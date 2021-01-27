/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.dataflow.outbox;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.dataflow.outbox.deduplication.ConsumedEventRepository;
import org.springframework.cloud.dataflow.outbox.deduplication.EventDeduplication;
import org.springframework.cloud.dataflow.outbox.service.ShipmentService;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;

@SpringBootApplication
@EnableConfigurationProperties(ShipmentProperties.class)
public class ShipmentConsumerApplication {

	private static final Log logger = LogFactory.getLog(ShipmentConsumerApplication.class);
	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static void main(String[] args) {
		SpringApplication.run(ShipmentConsumerApplication.class, args);
	}

	@Bean
	public EventDeduplication eventDeduplication(ConsumedEventRepository repository){
		return new EventDeduplication(repository);
	}

	@Bean
	public Consumer<Message<?>> shipmentConsumer(ShipmentService shipmentService, ShipmentProperties properties) {
		return message -> {

			//{"id":"64ac67e7-8d5e-474b-9703-a5efa7b2c54e",
			// "aggregate_id":"123",
			// "aggregate_type":"Customer",
			// "timestamp":1611318630626,
			// "type":"InvoiceCreated",
			// "payload":"{\"orderId\":2,\"invoiceDate\":\"2019-01-31T12:13:01\",\"invoiceValue\":69.97}"}"

			try {
				Map<String, String> map = objectMapper.readValue((byte[]) message.getPayload(), Map.class);

				UUID eventId = UUID.fromString(map.get("id"));
				String aggregateId = map.get("aggregate_id");
				String type = map.get("type");
				Instant timestamp = Instant.ofEpochMilli(message.getHeaders().get("kafka_receivedTimestamp", Long.class));
				logger.info("Map:" + map);
				JsonNode jsonNode = deserialize(map.get("payload"));

				shipmentService.onOrderEvent(eventId, type, aggregateId, jsonNode, timestamp);
			}
			catch (IOException e) {
				e.printStackTrace();
			}

			//message.getHeaders().entrySet().stream().forEach(h -> System.out.println("Header [" + h.getKey() + "]=" + h.getValue()));
			//System.out.println("Payload:" + message.getPayload());
		};
	}

	private static JsonNode deserialize(String event) {
		try {
			JsonNode eventPayload = objectMapper.readTree(event);
			return eventPayload.has("schema") ? eventPayload.get("payload") : eventPayload;
		}
		catch (IOException e) {
			throw new RuntimeException("Couldn't deserialize event", e);
		}

	}
}
