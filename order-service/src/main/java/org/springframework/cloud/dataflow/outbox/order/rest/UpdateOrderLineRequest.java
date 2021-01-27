package org.springframework.cloud.dataflow.outbox.order.rest;

import org.springframework.cloud.dataflow.outbox.order.model.OrderLineStatus;

/**
 * A value object that represents updating a {@link org.springframework.cloud.dataflow.outbox.order.model.OrderLine} status.
 */
public class UpdateOrderLineRequest {

	private OrderLineStatus newStatus;

	public OrderLineStatus getNewStatus() {
		return newStatus;
	}

	public void setNewStatus(OrderLineStatus newStatus) {
		this.newStatus = newStatus;
	}
}
