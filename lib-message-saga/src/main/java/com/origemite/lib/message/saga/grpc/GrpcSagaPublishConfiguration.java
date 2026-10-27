package com.origemite.lib.message.saga.grpc;

import com.origemite.lib.message.saga.SagaPublishPort;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.json.JsonMapper;

@Configuration
@ConditionalOnProperty(prefix = "origemite.message.saga.publish", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(SagaGrpcPublishProperties.class)
public class GrpcSagaPublishConfiguration {

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean(name = "sagaPublishChannel")
    public ManagedChannel sagaPublishChannel(SagaGrpcPublishProperties properties) {
        String target = properties.getTarget();
        if (target.startsWith("static://")) {
            target = target.substring("static://".length());
        }
        int colon = target.lastIndexOf(':');
        if (colon > 0) {
            String host = target.substring(0, colon);
            int port = Integer.parseInt(target.substring(colon + 1));
            return ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        }
        return ManagedChannelBuilder.forTarget(target).usePlaintext().build();
    }

    @Bean
    @ConditionalOnMissingBean(name = "sagaJsonMapper")
    public JsonMapper sagaJsonMapper() {
        return JsonMapper.builder().findAndAddModules(getClass().getClassLoader()).build();
    }

    @Bean
    @ConditionalOnMissingBean(SagaPublishPort.class)
    public SagaPublishPort sagaPublishPort(ManagedChannel sagaPublishChannel, JsonMapper sagaJsonMapper) {
        return new GrpcSagaPublishAdapter(
                SagaIngressGrpc.newBlockingStub(sagaPublishChannel),
                sagaJsonMapper
        );
    }
}
