-- 자기 이벤트 Outbox만. saga_instance / Inbox 없음.
CREATE TABLE IF NOT EXISTS event_outbox (
    id                BIGSERIAL PRIMARY KEY,
    event_id          VARCHAR(36)  NOT NULL UNIQUE,
    correlation_id    VARCHAR(36)  NOT NULL,
    type              VARCHAR(16)  NOT NULL,
    domain            VARCHAR(64)  NOT NULL,
    topic             VARCHAR(128) NOT NULL,
    payload_json      TEXT         NOT NULL,
    delivery_status   VARCHAR(16)  NOT NULL DEFAULT 'PENDING',
    created_at        TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    sent_at           TIMESTAMPTZ
);

CREATE INDEX IF NOT EXISTS ix_event_outbox_delivery_status_id
    ON event_outbox (delivery_status, id);

CREATE INDEX IF NOT EXISTS ix_event_outbox_correlation_id
    ON event_outbox (correlation_id);
