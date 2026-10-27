package com.origemite.lib.message.saga.kafka;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "origemite.message.saga.consume")
public class SagaKafkaConsumerProperties {

    private int concurrency = 1;

    public int getConcurrency() {
        return concurrency;
    }

    public void setConcurrency(int concurrency) {
        this.concurrency = concurrency;
    }
}
