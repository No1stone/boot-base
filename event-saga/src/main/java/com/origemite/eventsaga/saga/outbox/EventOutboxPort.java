package com.origemite.eventsaga.saga.outbox;

import java.util.List;

public interface EventOutboxPort {

    EventOutboxRecord save(EventOutboxRecord record);

    List<EventOutboxRecord> findPending(int limit);

    void markSent(Long id);

    void markFailed(Long id);
}
