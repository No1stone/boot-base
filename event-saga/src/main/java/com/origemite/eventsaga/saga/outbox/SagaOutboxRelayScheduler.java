package com.origemite.eventsaga.saga.outbox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SagaOutboxRelayScheduler {

    private static final Logger log = LoggerFactory.getLogger(SagaOutboxRelayScheduler.class);

    private final EventOutboxRelay relay;

    public SagaOutboxRelayScheduler(EventOutboxRelay relay) {
        this.relay = relay;
    }

    @Scheduled(fixedDelayString = "${origemite.message.saga.outbox.relay-delay-ms:2000}")
    public void relay() {
        int sent = relay.relayOnce();
        if (sent > 0) {
            log.debug("Outbox relayed count={}", sent);
        }
    }
}
