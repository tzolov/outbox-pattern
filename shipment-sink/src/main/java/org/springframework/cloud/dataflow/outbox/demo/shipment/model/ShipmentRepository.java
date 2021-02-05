package org.springframework.cloud.dataflow.outbox.demo.shipment.model;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Christian Tzolov
 */
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
}
