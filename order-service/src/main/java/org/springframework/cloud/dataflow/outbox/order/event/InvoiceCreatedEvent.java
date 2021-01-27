package org.springframework.cloud.dataflow.outbox.order.event;

import java.time.Instant;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.cloud.dataflow.outbox.event.ExportedEvent;
import org.springframework.cloud.dataflow.outbox.order.model.PurchaseOrder;

/**
 * A 'Customer' event that indicates an invoice has been created.
 */
public class InvoiceCreatedEvent implements ExportedEvent<String, JsonNode> {

	private static ObjectMapper mapper = new ObjectMapper();

	private final long customerId;
	private final JsonNode order;
	private final Instant timestamp;

	private InvoiceCreatedEvent(long customerId, JsonNode order) {
		this.customerId = customerId;
		this.order = order;
		this.timestamp = Instant.now();
	}

	public static InvoiceCreatedEvent of(PurchaseOrder order) {
		ObjectNode asJson = mapper.createObjectNode()
				.put("orderId", order.getId())
				.put("invoiceDate", order.getOrderDate().toString())
				.put("invoiceValue", order.getTotalValue());

		return new InvoiceCreatedEvent(order.getCustomerId(), asJson);
	}

	@Override
	public String getAggregateId() {
		return String.valueOf(customerId);
	}

	@Override
	public String getAggregateType() {
		return "Customer";
	}

	@Override
	public JsonNode getPayload() {
		return order;
	}

	@Override
	public String getType() {
		return "InvoiceCreated";
	}

	@Override
	public Instant getTimestamp() {
		return timestamp;
	}
}
