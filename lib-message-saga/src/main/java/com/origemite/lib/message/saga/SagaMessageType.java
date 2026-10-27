package com.origemite.lib.message.saga;

/**
 * 사가 메시지 유형. action 필드 없이 이 네 가지로만 분기한다.
 */
public enum SagaMessageType {
    CREATE,
    STATUS,
    FAIL,
    COMPLETE;

    public String topicSegment() {
        return name().toLowerCase();
    }

    public static SagaMessageType fromTopicSegment(String segment) {
        return SagaMessageType.valueOf(segment.toUpperCase());
    }
}
