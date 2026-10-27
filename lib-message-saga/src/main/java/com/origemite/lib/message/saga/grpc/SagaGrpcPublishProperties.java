package com.origemite.lib.message.saga.grpc;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "origemite.message.saga.publish")
public class SagaGrpcPublishProperties {

    /** event-saga gRPC 주소. 예: static://localhost:9090 */
    private String target = "static://localhost:9090";

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
