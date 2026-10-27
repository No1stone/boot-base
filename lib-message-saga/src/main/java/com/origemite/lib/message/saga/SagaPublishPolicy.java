package com.origemite.lib.message.saga;

import org.springframework.util.StringUtils;

/**
 * Outbox로 나갈 수 있는 자기 이벤트만 허용. 사가 상태(CREATE/STATUS)는 관리·발행하지 않는다.
 *
 * <ul>
 *   <li>성공 기준 — {@code COMPLETE} (기본 status {@code SUCCEEDED})</li>
 *   <li>실패·보상 — {@code FAIL} + status에 {@code COMPENSATE*} → 컨슈머가 “내 것”이면 보상</li>
 * </ul>
 */
public final class SagaPublishPolicy {

    private SagaPublishPolicy() {
    }

    public static boolean shouldPublish(SagaMessageType type, String status) {
        if (type == null) {
            return false;
        }
        return switch (type) {
            case COMPLETE -> true;
            case FAIL -> isCompensationDirective(status);
            case CREATE, STATUS -> false;
        };
    }

    public static boolean shouldPublish(SagaMessage message) {
        return message != null && shouldPublish(message.type(), message.status());
    }

    /** 발행 입구용. 비발행 type이면 예외 (상태 전용 입구 없음). */
    public static void requirePublishable(SagaMessageType type, String status) {
        if (!shouldPublish(type, status)) {
            throw new IllegalArgumentException(
                    "Outbox publishes only COMPLETE (success) or FAIL+COMPENSATE* (own compensate event). type="
                            + type + " status=" + status);
        }
    }

    public static boolean isCompensationDirective(String status) {
        if (!StringUtils.hasText(status)) {
            return false;
        }
        return status.toUpperCase().contains("COMPENSATE");
    }
}
