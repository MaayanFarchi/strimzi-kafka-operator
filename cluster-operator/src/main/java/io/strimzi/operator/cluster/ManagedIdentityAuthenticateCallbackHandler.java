/*
 * Copyright Strimzi authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.strimzi.operator.cluster;

import com.azure.core.credential.AccessToken;
import com.azure.core.credential.TokenCredential;
import com.azure.core.credential.TokenRequestContext;
import com.azure.identity.ManagedIdentityCredentialBuilder;
import com.google.common.base.Preconditions;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.security.auth.AuthenticateCallbackHandler;
import org.apache.kafka.common.security.oauthbearer.OAuthBearerToken;
import org.apache.kafka.common.security.oauthbearer.OAuthBearerTokenCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.AppConfigurationEntry;
import java.net.URI;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;

public class ManagedIdentityAuthenticateCallbackHandler implements AuthenticateCallbackHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ManagedIdentityAuthenticateCallbackHandler.class);

    private String eventHubScope;

    @Override
    public void configure(Map<String, ?> configs, String mechanism, List<AppConfigurationEntry> jaasConfigEntries) {
        Object boostrapServerObject = configs.get(BOOTSTRAP_SERVERS_CONFIG);
        Preconditions.checkNotNull(boostrapServerObject, "Failed to find `%s` in configuration", BOOTSTRAP_SERVERS_CONFIG);
        @SuppressWarnings("unchecked")
        List<String> bootstrapServerList = (List<String>) boostrapServerObject;
        Preconditions.checkState(bootstrapServerList.size() == 1, "Supporting only one boostrap server, found `%s`", bootstrapServerList.size());
        String bootstrapServer = bootstrapServerList.get(0);
        eventHubScope = parseScope(bootstrapServer);
    }

    public void handle(Callback[] callbacks) throws UnsupportedCallbackException {
        for (Callback callback: callbacks) {
            if (callback instanceof OAuthBearerTokenCallback) {
                OAuthBearerToken token = getOAuthBearerToken();
                OAuthBearerTokenCallback oauthCallback = (OAuthBearerTokenCallback) callback;
                oauthCallback.token(token);
            } else {
                throw new UnsupportedCallbackException(callback);
            }
        }
    }

    private OAuthBearerToken getOAuthBearerToken() {
        TokenRequestContext tokenRequestContext = new TokenRequestContext().addScopes(eventHubScope);
        TokenCredential credential = new ManagedIdentityCredentialBuilder().build();
        Mono<AccessToken> tokenMono = credential.getToken(tokenRequestContext);
        AccessToken token = tokenMono.block(Duration.of(2, ChronoUnit.SECONDS));
        Preconditions.checkNotNull(token, "Failed to get token");
        String accessToken = token.getToken();
        LOGGER.info("Got token ? " + !token.isExpired());
        long expireAtInMs = token.getExpiresAt().toInstant().toEpochMilli();
        return new OauthBearerTokenImp(accessToken, expireAtInMs);
    }

    public void close() throws KafkaException {
        // NOOP
    }

    public static String parseScope(String bootsrapServer) {
        bootsrapServer = bootsrapServer.replaceAll("\\[|]", "");
        URI uri = URI.create("https://" + bootsrapServer);
        return uri.getScheme() + "://" + uri.getHost();
    }

    public static class OauthBearerTokenImp implements OAuthBearerToken {
        private final String token;
        private final long lifetimeMs;

        public OauthBearerTokenImp(final String token, long expiresOn) {
            this.token = token;
            lifetimeMs = expiresOn;
        }

        @Override
        public String value() {
            return token;
        }

        @Override
        public Set<String> scope() {
            return Collections.emptySet();
        }

        @Override
        public long lifetimeMs() {
            return lifetimeMs;
        }

        @Override
        public String principalName() {
            return null;
        }

        @Override
        public Long startTimeMs() {
            return null;
        }
    }
}

