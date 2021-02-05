package org.springframework.cloud.dataflow.outbox.service.event;

import java.time.Instant;

/**
 * Describes an event that should be exported via the "outbox" table.
 *
 *
 * The ExportedEvent interface is parameterized to allow the application to designate the Java types used by several
 * attributes emitted by the event. It’s important that for a given Spring application, all implementations of the
 * ExportedEvent interface must use the same parameter types or a build failure will be thrown since all events will
 * use the same underlying database table.
 *
 * @author Chris Cranford
 */
public interface ExportedEvent<I, P> {

	/**
	 * The id of the aggregate affected by a given event.  For example, the order id in case of events
	 * relating to an order, or order lines of that order.  This is used to ensure ordering of events
	 * within an aggregate type.
	 */
	I getAggregateId();

	/**
	 * The type of the aggregate affected by the event.  For example, "order" in case of events relating
	 * to an order, or order lines of that order.  This is used as the topic name.
	 */
	String getAggregateType();

	/**
	 * The type of an event.  For example, "Order Created" or "Order Line Cancelled" for events that
	 * belong to an given aggregate type such as "order".
	 */
	String getType();

	/**
	 * The timestamp at which the event occurred.
	 */
	Instant getTimestamp();

	/**
	 * The event payload.
	 */
	P getPayload();
}
