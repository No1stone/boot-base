package com.origemite.eventsaga.saga.persistence.repository;

import com.origemite.eventsaga.saga.persistence.entity.SagaOutboxEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SagaOutboxRepository extends JpaRepository<SagaOutboxEntity, Long> {
    List<SagaOutboxEntity> findByDeliveryStatusOrderByIdAsc(String deliveryStatus, Pageable pageable);
}
