package org.springframework.cloud.dataflow.outbox.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.dataflow.outbox.ShipmentConsumerApplication;
import org.springframework.cloud.dataflow.outbox.deduplication.EventDeduplication;
import org.springframework.cloud.dataflow.outbox.model.Shipment;
import org.springframework.cloud.dataflow.outbox.model.ShipmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Christian Tzolov
 */
@Service
public class ShipmentService {

	private static final Log logger = LogFactory.getLog(ShipmentConsumerApplication.class);

	private final EventDeduplication deduplicationService;

	private final ShipmentRepository shipmentRepository;

	@Autowired
	public ShipmentService(EventDeduplication deduplicationService, ShipmentRepository shipmentRepository) {
		this.deduplicationService = deduplicationService;
		this.shipmentRepository = shipmentRepository;
	}

	@Transactional
	public void onOrderEvent(UUID eventId, String eventType, String key, JsonNode payload, Instant ts) {

		if (this.deduplicationService.alreadyProcessed(eventId)) {
			logger.warn("Event with UUID {" + eventId + "} was already retrieved, ignoring it");
			return;
		}

		if (eventType.equals("OrderCreated")) {
			this.orderCreated(payload);
		}
		else if (eventType.equals("OrderLineUpdated")) {
			this.orderLineUpdated(payload);
		}
		else {
			logger.warn("Unknown event type: " + eventType);
		}

		this.deduplicationService.processed(eventId);
	}

	private void orderCreated(JsonNode payload) {
		logger.info("Order created: " + payload.toPrettyString());

		final long orderId = payload.get("id").asLong();
		final long customerId = payload.get("customerId").asLong();
		final LocalDateTime orderDate = LocalDateTime.parse(payload.get("orderDate").asText());

		this.shipmentRepository.save(new Shipment(customerId, orderId, orderDate));

	}

	private void orderLineUpdated(JsonNode payload) {
		logger.info("Order Line updated: " + payload.toPrettyString());
	}
}
