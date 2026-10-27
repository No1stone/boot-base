package com.origemite.eventsaga.saga.orchestration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "origemite.message.saga.orchestration")
public class SagaOrchestrationProperties {

    /** Saga 서버 컨슈머 그룹. */
    private String group = "event-saga";

    /** Saga가 구독할 토픽 (시작·참여자 결과 등). */
    private List<String> topics = new ArrayList<>();

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics != null ? topics : new ArrayList<>();
    }
}
