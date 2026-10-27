package com.origemite.lib.message.routing;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * 라우팅(실시간·명령)용 메시지 봉투.
 * 사가 envelope와 별개다. 구독에 필요한 필드는 {@code data}에 넣는다.
 * 멱등·유효시간은 봉투 필드로 실어 보내고, 연결 유지는 Rabbit 어댑터가 담당한다.
 */
public record RoutingMessage(
        String messageId,
        String messageType,
        Instant occurredAt,
        String correlationId,
        String idempotencyKey,
        Instant expiresAt,
        Map<String, String> headers,
        Object data
) {

    public boolean isExpired() {
        return expiresAt != null && Instant.now().isAfter(expiresAt);
    }

    public static Builder builder(String messageType) {
        return new Builder(messageType);
    }

    public static final class Builder {
        private String messageId = UUID.randomUUID().toString();
        private final String messageType;
        private Instant occurredAt = Instant.now();
        private String correlationId;
        private String idempotencyKey;
        private Instant expiresAt;
        private Map<String, String> headers = Map.of();
        private Object data;

        private Builder(String messageType) {
            this.messageType = messageType;
        }

        public Builder messageId(String messageId) {
            this.messageId = messageId;
            return this;
        }

        public Builder occurredAt(Instant occurredAt) {
            this.occurredAt = occurredAt;
            return this;
        }

        public Builder correlationId(String correlationId) {
            this.correlationId = correlationId;
            return this;
        }

        public Builder idempotencyKey(String idempotencyKey) {
            this.idempotencyKey = idempotencyKey;
            return this;
        }

        public Builder expiresAt(Instant expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            this.headers = headers != null ? Map.copyOf(headers) : Map.of();
            return this;
        }

        public Builder data(Object data) {
            this.data = data;
            return this;
        }

        public RoutingMessage build() {
            if (messageType == null || messageType.isBlank()) {
                throw new IllegalArgumentException("messageType must not be blank");
            }
            String idem = idempotencyKey != null && !idempotencyKey.isBlank() ? idempotencyKey : messageId;
            return new RoutingMessage(
                    messageId, messageType, occurredAt, correlationId, idem, expiresAt, headers, data);
        }
    }
}
