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


    @Description("SASL mechanism used for client connections. This may be any mechanism for which a security provider is available.")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public String getSaslMechanism() {
        return saslMechanism;
    }

    public void setSaslMechanism(String saslMechanism) {
        this.saslMechanism = saslMechanism;
    }


    @Description("As part of enabling any of the SASL authentication mechanisms, " +
            "you must provide Java Authentication and Authorization Service (JAAS) configurations. " +
            "You can check kafka documentation for details under 'Authentication with SASL'.\n")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public String getSaslJaasConfig() {
        return saslJaasConfig;
    }

    public void setSaslJaasConfig(String saslJaasConfig) {
        this.saslJaasConfig = saslJaasConfig;
    }

    @Description("The fully qualified name of a SASL login callback handler class that implements the AuthenticateCallbackHandler interface," +
            "you can add your own impl by extending the Kafka client image and putting your own jar under /opt/kafka/libs.")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public String getSaslLoginCallbackHandlerClass() {
        return saslLoginCallbackHandlerClass;
    }

    public void setSaslLoginCallbackHandlerClass(String saslLoginCallbackHandlerClass) {
        this.saslLoginCallbackHandlerClass = saslLoginCallbackHandlerClass;
    }
}
