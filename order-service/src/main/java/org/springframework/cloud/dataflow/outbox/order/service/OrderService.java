package org.springframework.cloud.dataflow.outbox.order.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.cloud.dataflow.outbox.event.OutboxEventSender;
import org.springframework.cloud.dataflow.outbox.order.event.InvoiceCreatedEvent;
import org.springframework.cloud.dataflow.outbox.order.event.OrderCreatedEvent;
import org.springframework.cloud.dataflow.outbox.order.event.OrderLineUpdatedEvent;
import org.springframework.cloud.dataflow.outbox.order.model.EntityNotFoundException;
import org.springframework.cloud.dataflow.outbox.order.model.OrderLineStatus;
import org.springframework.cloud.dataflow.outbox.order.model.PurchaseOrder;
import org.springframework.cloud.dataflow.outbox.order.model.PurchaseOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Christian Tzolov
 */
@Service
public class OrderService {

	private final PurchaseOrderRepository orderRepository;

	private final OutboxEventSender outboxEventSender;

	@Autowired
	public OrderService(PurchaseOrderRepository orderRepository, OutboxEventSender outboxEventSender) {
		this.orderRepository = orderRepository;
		this.outboxEventSender = outboxEventSender;
	}

	@Transactional
	public PurchaseOrder addOrder(PurchaseOrder order) {
		PurchaseOrder order2 = orderRepository.save(order);

		// Fire events for newly created PurchaseOrder
		this.outboxEventSender.fire(OrderCreatedEvent.of(order));
		this.outboxEventSender.fire(InvoiceCreatedEvent.of(order));
		return order2;
	}

	@Transactional
	public PurchaseOrder updateOrderLine(long orderId, long orderLineId, OrderLineStatus newStatus) {
		Optional<PurchaseOrder> order = orderRepository.findById(orderId);
		if (!order.isPresent()) {
			throw new EntityNotFoundException("Order with id " + orderId + " could not be found");
		}

		OrderLineStatus oldStatus = order.get().updateOrderLine(orderLineId, newStatus);

		outboxEventSender.fire(OrderLineUpdatedEvent.of(orderId, orderLineId, newStatus, oldStatus));

		return order.get();
	}
}
