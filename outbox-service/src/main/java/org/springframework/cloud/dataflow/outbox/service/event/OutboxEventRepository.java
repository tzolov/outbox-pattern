package org.springframework.cloud.dataflow.outbox.service.event;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Christian Tzolov
 */
public interface OutboxEventRepository extends JpaRepository<OutboxEvent, UUID> {
}
