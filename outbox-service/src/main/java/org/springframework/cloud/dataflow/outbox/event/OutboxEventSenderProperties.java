package org.springframework.cloud.dataflow.outbox.event;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Christian Tzolov
 */
@ConfigurationProperties(prefix = "outbox.event")
public class OutboxEventSenderProperties {

	/**
	 * Whether the outbox entry is removed after having been inserted.
	 * The removal of the entry does not impact the Debezium connector from being able to emit CDC events.
	 * This is used as a way to keep the table’s underlying storage from growing over time.
	 */
	private boolean removeAfterInsert;

	public boolean isRemoveAfterInsert() {
		return removeAfterInsert;
	}

	public void setRemoveAfterInsert(boolean removeAfterInsert) {
		this.removeAfterInsert = removeAfterInsert;
	}
}
