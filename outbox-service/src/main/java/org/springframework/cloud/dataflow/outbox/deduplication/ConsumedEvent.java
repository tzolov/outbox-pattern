/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.springframework.cloud.dataflow.outbox.deduplication;

import java.time.Instant;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ConsumedEvent {

	@Id
	private UUID eventId;

	private Instant timeOfReceiving;

	public ConsumedEvent() {
	}

	public ConsumedEvent(UUID eventId, Instant timeOfReceiving) {
		this.eventId = eventId;
		this.timeOfReceiving = timeOfReceiving;
	}

	public UUID getEventId() {
		return eventId;
	}

	public void setEventId(UUID eventId) {
		this.eventId = eventId;
	}

	public Instant getTimeOfReceiving() {
		return timeOfReceiving;
	}

	public void setTimeOfReceiving(Instant timeOfReceiving) {
		this.timeOfReceiving = timeOfReceiving;
	}
}
