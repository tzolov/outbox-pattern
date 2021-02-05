package org.springframework.cloud.dataflow.outbox.demo.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.dataflow.outbox.service.event.OutboxEventRepository;
import org.springframework.cloud.dataflow.outbox.service.event.OutboxEventSender;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories({"org.springframework.cloud.dataflow.outbox.demo.order", "org.springframework.cloud.dataflow.outbox.service.event"})
@EntityScan({"org.springframework.cloud.dataflow.outbox.demo.order", "org.springframework.cloud.dataflow.outbox.service.event"})
public class OrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderApplication.class, args);
	}

	@Bean
	public OutboxEventSender outboxEventSender(OutboxEventRepository outboxEventRepository) {
		return new OutboxEventSender(outboxEventRepository, true);
	}

}
