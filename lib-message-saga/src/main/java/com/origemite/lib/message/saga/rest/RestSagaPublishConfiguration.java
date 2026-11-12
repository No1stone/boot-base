package com.origemite.lib.message.saga.rest;

import com.origemite.lib.message.saga.SagaPublishPort;
import com.origemite.lib.message.saga.SagaPublishProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@ConditionalOnClass(RestClient.class)
@ConditionalOnProperty(prefix = "origemite.message.saga.publish", name = "enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnProperty(prefix = "origemite.message.saga.publish", name = "transport", havingValue = "rest", matchIfMissing = true)
@EnableConfigurationProperties(SagaPublishProperties.class)
public class RestSagaPublishConfiguration {

    @Bean
    @ConditionalOnMissingBean(SagaPublishPort.class)
    public SagaPublishPort sagaPublishPort(SagaPublishProperties properties) {
        RestClient client = RestClient.builder()
                .baseUrl(properties.getBaseUrl())
                .build();
        return new RestSagaPublishAdapter(client);
    }
}
