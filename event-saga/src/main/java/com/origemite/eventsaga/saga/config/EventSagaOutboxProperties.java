package com.origemite.eventsaga.saga.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "origemite.message.saga.outbox")
public class EventSagaOutboxProperties {

    private int batchSize = 50;
    private long relayDelayMs = 2000;

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public long getRelayDelayMs() {
        return relayDelayMs;
    }

    public void setRelayDelayMs(long relayDelayMs) {
        this.relayDelayMs = relayDelayMs;
    }
}
