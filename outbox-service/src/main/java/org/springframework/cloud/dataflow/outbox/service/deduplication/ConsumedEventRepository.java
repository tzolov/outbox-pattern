package org.springframework.cloud.dataflow.outbox.service.deduplication;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Christian Tzolov
 */
public interface ConsumedEventRepository extends JpaRepository<ConsumedEvent, UUID> {
}
