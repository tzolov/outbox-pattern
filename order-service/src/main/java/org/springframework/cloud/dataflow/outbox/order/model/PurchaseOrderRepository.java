package org.springframework.cloud.dataflow.outbox.order.model;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Christian Tzolov
 */
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
}
