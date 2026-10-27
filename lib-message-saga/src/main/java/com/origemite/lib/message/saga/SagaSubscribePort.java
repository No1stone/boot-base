package com.origemite.lib.message.saga;

public interface SagaSubscribePort {

    void subscribe(String topic, String consumerGroup);
}
