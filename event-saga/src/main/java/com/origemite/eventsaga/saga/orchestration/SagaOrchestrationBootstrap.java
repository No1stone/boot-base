package com.origemite.eventsaga.saga.orchestration;

import com.origemite.lib.message.saga.SagaSubscribePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * Saga 서버 = Kafka 컨슈머. 설정 토픽을 구독한다.
 * Handler({@link com.origemite.lib.message.saga.SagaEventHandler})가 진행·보상을 결정하고 Outbox로 다음을 발행한다.
 */
@Component
@ConditionalOnProperty(prefix = "origemite.message.saga.consume", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(SagaOrchestrationProperties.class)
public class SagaOrchestrationBootstrap implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(SagaOrchestrationBootstrap.class);

    private final SagaSubscribePort sagaSubscribePort;
    private final SagaOrchestrationProperties properties;

    public SagaOrchestrationBootstrap(
            SagaSubscribePort sagaSubscribePort,
            SagaOrchestrationProperties properties
    ) {
        this.sagaSubscribePort = sagaSubscribePort;
        this.properties = properties;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (CollectionUtils.isEmpty(properties.getTopics())) {
            log.warn("Saga consumer enabled but origemite.message.saga.orchestration.topics is empty");
            return;
        }
        String group = properties.getGroup();
        for (String topic : properties.getTopics()) {
            sagaSubscribePort.subscribe(topic, group);
            log.info("Saga server subscribed topic={} group={}", topic, group);
        }
    }
}
