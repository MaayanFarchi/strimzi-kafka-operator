/*
 * Copyright Strimzi authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.strimzi.api.kafka.model.authentication;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.strimzi.api.kafka.model.Constants;
import io.strimzi.crdgenerator.annotations.Description;
import io.strimzi.crdgenerator.annotations.DescriptionFile;
import io.sundr.builder.annotations.Buildable;
import lombok.EqualsAndHashCode;

@DescriptionFile
@Buildable(
        editableEnabled = false,
        builderPackage = Constants.FABRIC8_KUBERNETES_API
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode
public class KafkaClientCustomAuthentication extends KafkaClientAuthentication {
    private static final long serialVersionUID = 1L;

    public static final String TYPE_CUSTOM = "custom";
    private String saslMechanism;
    private String saslJaasConfig;
    private String saslLoginCallbackHandlerClass;

    @Override
    @Description("Must be `" + TYPE_CUSTOM + "`")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getType() {
        return TYPE_CUSTOM;
    }


    @Description("Options are; OAUTHBEARER/PLAIN")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getSaslMechanism() {
        return saslMechanism;
    }

    public void setSaslMechanism(String saslMechanism) {
        this.saslMechanism = saslMechanism;
    }


    @Description("Default is org.apache.kafka.common.security.oauthbearer.OAuthBearerLoginModule")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getSaslJaasConfig() {
        return saslJaasConfig;
    }

    public void setSaslJaasConfig(String saslJaasConfig) {
        this.saslJaasConfig = saslJaasConfig;
    }

    @Description("The call back login class you extend the image with")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getSaslLoginCallbackHandlerClass() {
        return saslLoginCallbackHandlerClass;
    }

    public void setSaslLoginCallbackHandlerClass(String saslLoginCallbackHandlerClass) {
        this.saslLoginCallbackHandlerClass = saslLoginCallbackHandlerClass;
    }
}
