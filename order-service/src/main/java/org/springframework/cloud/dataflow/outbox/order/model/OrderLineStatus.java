package org.springframework.cloud.dataflow.outbox.order.model;

/**
 * Various statuses in which a {@link OrderLine} may be within.
 */
public enum OrderLineStatus {
	ENTERED,
	CANCELLED,
	SHIPPED
}
