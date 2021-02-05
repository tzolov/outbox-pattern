package org.springframework.cloud.dataflow.outbox.demo.order.model;

/**
 * An exception that indicates an entity could not be found.
 */
public class EntityNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -1L;

	public EntityNotFoundException(String message) {
		super(message);
	}
}
