package com.origemite.lib.message.saga;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "origemite.message.saga.publish")
public class SagaPublishProperties {

    private boolean enabled = true;

    /** rest (기본, Kafka 불필요) | grpc */
    private String transport = "rest";

    /** REST: event-saga HTTP base. */
    private String baseUrl = "http://localhost:8095";

    /** gRPC: host:port */
    private String target = "localhost:9095";

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
