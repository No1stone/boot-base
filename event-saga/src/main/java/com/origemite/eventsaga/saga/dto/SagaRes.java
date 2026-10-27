package com.origemite.eventsaga.saga.dto;

/**
 * 사가 입구 응답 DTO.
 */
public final class SagaRes {

    private SagaRes() {
    }

    public static class Accepted {
        private String eventId;
        private String sagaId;
        private boolean accepted;
        private String message = "ok";

        public Accepted() {
        }

        public Accepted(String eventId, String sagaId, boolean accepted) {
            this.eventId = eventId;
            this.sagaId = sagaId;
            this.accepted = accepted;
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

        public boolean isAccepted() {
            return accepted;
        }

        public void setAccepted(boolean accepted) {
            this.accepted = accepted;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
