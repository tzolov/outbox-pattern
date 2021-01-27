package org.springframework.cloud.dataflow.outbox.event;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * The outbox event entity.
 *
 * The contents of the {@link ExportedEvent} will be replicated to this entity definition and persisted to
 * the database in order for Debezium to capture the event.
 *
 * @author Chris Cranford
 */
@Entity
public class OutboxEvent {
	@Id
	@GeneratedValue
	private UUID id;

	@NotNull
	private String aggregateType;

	@NotNull
	private String aggregateId;

	@NotNull
	private String type;

	@NotNull
	private Long timestamp;

	@NotNull
	private String payload;

	public OutboxEvent() {
	}

	public OutboxEvent(String aggregateType, String aggregateId, String type, String payload, Long timestamp) {
		this.aggregateType = aggregateType;
		this.aggregateId = aggregateId;
		this.type = type;
		this.payload = payload;
		this.timestamp = timestamp;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getAggregateType() {
		return aggregateType;
	}

	public void setAggregateType(String aggregateType) {
		this.aggregateType = aggregateType;
	}

	public String getAggregateId() {
		return aggregateId;
	}

	public void setAggregateId(String aggregateId) {
		this.aggregateId = aggregateId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}
}
