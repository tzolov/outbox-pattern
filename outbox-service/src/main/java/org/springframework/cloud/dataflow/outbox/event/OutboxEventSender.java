package org.springframework.cloud.dataflow.outbox.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Christian Tzolov
 */
public class OutboxEventSender {

	private final OutboxEventRepository outboxEventRepository;

	/**
	 * Whether the outbox entry is removed after having been inserted.
	 * The removal of the entry does not impact the Debezium connector from being able to emit CDC events.
	 * This is used as a way to keep the table’s underlying storage from growing over time.
	 */
	private final boolean removeEventAfterInsert;

	public OutboxEventSender(OutboxEventRepository outboxEventRepository, boolean removeEventAfterInsert) {
		this.outboxEventRepository = outboxEventRepository;
		this.removeEventAfterInsert = removeEventAfterInsert;
	}

	public void fire(ExportedEvent<?, ?> event) {

		// Create an OutboxEvent object based on the ExportedEvent interface
		final OutboxEvent outboxEvent = new OutboxEvent(
				event.getAggregateType(),
				"" + event.getAggregateId(),
				event.getType(),
				payloadAsString(event.getPayload()),
				event.getTimestamp().toEpochMilli()
		);

		// We want the events table to remain empty; however this triggers both an INSERT and DELETE
		// in the database transaction log which is sufficient for Debezium to process the event.
		OutboxEvent savedOutboxEvent = this.outboxEventRepository.save(outboxEvent);
		if (removeEventAfterInsert) {
			this.outboxEventRepository.delete(savedOutboxEvent);
		}
	}

	private static String payloadAsString(Object jsonNode) {
		try {
			return new ObjectMapper().writeValueAsString(jsonNode);
		}
		catch (JsonProcessingException e) {
			throw new IllegalArgumentException(e);
		}
	}
}
