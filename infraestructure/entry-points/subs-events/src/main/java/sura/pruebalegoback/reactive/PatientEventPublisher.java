package sura.pruebalegoback.reactive;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import reactor.rabbitmq.ExchangeSpecification;
import reactor.rabbitmq.OutboundMessage;
import reactor.rabbitmq.QueueSpecification;
import reactor.rabbitmq.Sender;
import reactor.rabbitmq.BindingSpecification;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import sura.pruebalegoback.domain.patient.event.PatientCreatedEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class PatientEventPublisher {
	private final ObjectMapper objectMapper;
	private final Sender sender;

	private static final String EXCHANGE = "patient.events";
	private static final String ROUTING_KEY = "patient.created";
	private static final String QUEUE = "patient.created.queue";

	@PostConstruct
	public void init() {
		ExchangeSpecification exchangeSpec = ExchangeSpecification.exchange(EXCHANGE)
				.type(BuiltinExchangeType.DIRECT.getType())
				.durable(true);

		QueueSpecification queueSpec = QueueSpecification.queue(QUEUE)
				.durable(true);

		sender.declareExchange(exchangeSpec)
				.then(sender.declareQueue(queueSpec))
				.then(sender.bind(
						BindingSpecification.binding(EXCHANGE, ROUTING_KEY, QUEUE)
				))
				.subscribe(
						v -> log.info("RabbitMQ infrastructure created successfully"),
						error -> log.error("Error creating RabbitMQ infrastructure", error)
				);
	}

	public Mono<Void> publish(PatientCreatedEvent event) {
		return Mono.fromCallable(() -> {
					try {
						byte[] body = objectMapper.writeValueAsBytes(event);
						AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
								.contentType("application/json")
								.deliveryMode(2) 
								.build();
						return new OutboundMessage(EXCHANGE, ROUTING_KEY, props, body);
					} catch (Exception e) {
						throw new RuntimeException("Error serializing event", e);
					}
				})
				.flatMap(message -> sender.send(Mono.just(message)))
				.doOnSuccess(v -> log.info("Event published successfully: {}", event))
				.doOnError(error -> log.error("Error publishing event", error))
				.onErrorResume(error -> {
					log.error("Failed to publish event, attempting recovery", error);
					return Mono.empty();
				});
	}
}