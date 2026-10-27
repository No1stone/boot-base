package com.origemite.eventsaga.saga.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Map;

/**
 * Outbox 발행 입구 DTO. COMPLETE 또는 FAIL+COMPENSATE* 만.
 */
public final class SagaReq {

    private SagaReq() {
    }

    /** 발행 본문. */
    public static class Ingress {
        @NotBlank
        private String type;
        private String eventId;
        private String sagaId;
        @NotBlank
        private String domain;
        private String step;
        private String status;
        private Map<String, Object> payload;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getEventId() {
            return eventId;
        }

        public void setEventId(String eventId) {
            this.eventId = eventId;
        }

        public String getSagaId() {
            return sagaId;
        }

        public void setSagaId(String sagaId) {
            this.sagaId = sagaId;
        }

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public String getStep() {
            return step;
        }

        public void setStep(String step) {
            this.step = step;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Map<String, Object> getPayload() {
            return payload;
        }

        public void setPayload(Map<String, Object> payload) {
            this.payload = payload;
        }
    }
}
