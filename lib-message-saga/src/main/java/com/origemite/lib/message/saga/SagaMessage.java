package com.origemite.lib.message.saga;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * Kafka 사가 봉투. 페이로드로 소비를 끝내는 것을 우선한다 (콜백 API 없음).
 *
 * <p>ID: {@code eventId}(메시지 UUID) · {@code sagaId}(흐름 UUID, CREATE 시 로컬 발급).
 * DB Long PK는 서버 테이블 대리키이며 메시지에 신지 않아도 된다.
 */
public record SagaMessage(
        String eventId,
        String sagaId,
        SagaMessageType type,
        String domain,
        String step,
        String status,
        Instant occurredAt,
        Map<String, String> headers,
        Object payload
) {

    public static Builder builder(SagaMessageType type, String domain) {
        return new Builder(type, domain);
    }

    public static final class Builder {
        private String eventId = UUID.randomUUID().toString();
        private String sagaId;
        private final SagaMessageType type;
        private final String domain;
        private String step;
        private String status;
        private Instant occurredAt = Instant.now();
        private Map<String, String> headers = Map.of();
        private Object payload;

        private Builder(SagaMessageType type, String domain) {
            this.type = type;
            this.domain = domain;
        }

        public Builder eventId(String eventId) {
            this.eventId = eventId;
            return this;
        }

        public Builder sagaId(String sagaId) {
            this.sagaId = sagaId;
            return this;
        }

        /** CREATE 때 sagaId가 없으면 eventId와 동일하게 둔다. */
        public Builder ensureSagaId() {
            if (sagaId == null || sagaId.isBlank()) {
                this.sagaId = this.eventId;
            }
            return this;
        }

        public Builder step(String step) {
            this.step = step;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder occurredAt(Instant occurredAt) {
            this.occurredAt = occurredAt;
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            this.headers = headers != null ? Map.copyOf(headers) : Map.of();
            return this;
        }

        public Builder payload(Object payload) {
            this.payload = payload;
            return this;
        }

        public SagaMessage build() {
            if (type == null) {
                throw new IllegalArgumentException("type must not be null");
            }
            if (domain == null || domain.isBlank()) {
                throw new IllegalArgumentException("domain must not be blank");
            }
            if (eventId == null || eventId.isBlank()) {
                throw new IllegalArgumentException("eventId must not be blank");
            }
            if (type == SagaMessageType.CREATE) {
                ensureSagaId();
            } else if (sagaId == null || sagaId.isBlank()) {
                throw new IllegalArgumentException("sagaId required for type=" + type);
            }
            return new SagaMessage(eventId, sagaId, type, domain, step, status, occurredAt, headers, payload);
        }
    }
}
