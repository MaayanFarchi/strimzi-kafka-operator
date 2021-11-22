package io.strimzi.api.kafka.model.authentication;

import com.fasterxml.jackson.annotation.JsonInclude;

public class KafkaClientCustomAuthentication extends KafkaClientAuthentication {

    public static final String TYPE_CUSTOM = "custom";
    private String saslMechanism;
    private String securityProtocol;
    private String saslJaasConfig;
    private String saslLoginCallbackHandlerClass;

    @Override
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getType() {
        return TYPE_CUSTOM;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getSaslMechanism() {
        return saslMechanism;
    }

    public void setSaslMechanism(String saslMechanism) {
        this.saslMechanism = saslMechanism;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getSecurityProtocol() {
        return securityProtocol;
    }

    public void setSecurityProtocol(String securityProtocol) {
        this.securityProtocol = securityProtocol;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getSaslJaasConfig() {
        return saslJaasConfig;
    }

    public void setSaslJaasConfig(String saslJaasConfig) {
        this.saslJaasConfig = saslJaasConfig;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getSaslLoginCallbackHandlerClass() {
        return saslLoginCallbackHandlerClass;
    }

    public void setSaslLoginCallbackHandlerClass(String saslLoginCallbackHandlerClass) {
        this.saslLoginCallbackHandlerClass = saslLoginCallbackHandlerClass;
    }
}
