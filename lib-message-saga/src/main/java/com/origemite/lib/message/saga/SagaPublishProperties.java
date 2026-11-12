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

    /** gRPC 연결 타임아웃 (Netty CONNECT_TIMEOUT). REST connect 와 대응. */
    private java.time.Duration connectTimeout = java.time.Duration.ofSeconds(5);

    /** gRPC 호출 deadline (블로킹 stub). REST read 와 대응. */
    private java.time.Duration deadline = java.time.Duration.ofSeconds(3);

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

    public java.time.Duration getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(java.time.Duration connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public java.time.Duration getDeadline() {
        return deadline;
    }

    public void setDeadline(java.time.Duration deadline) {
        this.deadline = deadline;
    }
}
