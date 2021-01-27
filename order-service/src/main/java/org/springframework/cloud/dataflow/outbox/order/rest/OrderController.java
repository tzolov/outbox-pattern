package org.springframework.cloud.dataflow.outbox.order.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.dataflow.outbox.order.model.PurchaseOrder;
import org.springframework.cloud.dataflow.outbox.order.service.OrderService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Christian Tzolov
 */
@RestController
public class OrderController {

	private final OrderService orderService;

	public OrderController(@Autowired OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping("/orders")
	public OrderOperationResponse addOrder(@RequestBody CreateOrderRequest createOrderRequest) {
		PurchaseOrder order = createOrderRequest.toOrder();
		order = this.orderService.addOrder(order);
		return OrderOperationResponse.from(order);
	}

	@PutMapping("/orders/{orderId}/lines/{orderLineId}")
	public OrderOperationResponse updateOrderLine(@PathVariable long orderId, @PathVariable long orderLineId, @RequestBody UpdateOrderLineRequest request) {
		PurchaseOrder updated = this.orderService.updateOrderLine(orderId, orderLineId, request.getNewStatus());
		return OrderOperationResponse.from(updated);
	}
}
