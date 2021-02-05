package org.springframework.cloud.dataflow.outbox.service.deduplication;

import java.time.Instant;
import java.util.UUID;

/**
 * Helper service that can be used to keep track of the processed events to prevent duplications.
 *
 * Services that use the EventDeduplication should create the Bean:
 * <code>
 *  @Bean
 *    public EventDeduplication eventDeduplication(ConsumedEventRepository repository){
 * 	    return new EventDeduplication(repository);
 *    }
 * </code>
 *
 * If necessary use the @EnableJpaRepositories({"org.springframework.cloud.dataflow.outbox.deduplication", ...}) and
 * @EntityScan({"org.springframework.cloud.dataflow.outbox.deduplication", ...}) annotation to configure
 * the deduplication entities and repository.
 *
 * @author Christian Tzolov
 */
public class EventDeduplication {

	private final ConsumedEventRepository repository;

	public EventDeduplication(ConsumedEventRepository repository) {
		this.repository = repository;
	}

	public boolean alreadyProcessed(UUID eventId) {
		return this.repository.findById(eventId).isPresent();
	}

	public void processed(UUID eventId) {
		this.repository.save(new ConsumedEvent(eventId, Instant.now()));
	}
}
