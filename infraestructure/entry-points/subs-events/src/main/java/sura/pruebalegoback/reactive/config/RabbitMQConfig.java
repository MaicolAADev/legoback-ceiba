// package sura.pruebalegoback.reactive.config;

// import com.rabbitmq.client.Connection;
// import com.rabbitmq.client.ConnectionFactory;
// import io.projectreactor.rabbitmq.*;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import reactor.core.publisher.Mono;

// @Configuration
// public class RabbitMQConfig {
// 	@Value("${spring.rabbitmq.host:localhost}")
// 	private String host;

// 	@Value("${spring.rabbitmq.port:5672}")
// 	private int port;

// 	@Value("${spring.rabbitmq.username:guest}")
// 	private String username;

// 	@Value("${spring.rabbitmq.password:guest}")
// 	private String password;

// 	@Bean
// 	public ConnectionFactory connectionFactory() {
// 		ConnectionFactory factory = new ConnectionFactory();
// 		factory.setHost(host);
// 		factory.setPort(port);
// 		factory.setUsername(username);
// 		factory.setPassword(password);
// 		factory.setAutomaticRecoveryEnabled(true);
// 		return factory;
// 	}

// 	@Bean
// 	public Mono<Connection> connectionMono(ConnectionFactory connectionFactory) {
// 		return Mono.fromCallable(() -> connectionFactory.newConnection("reactor-rabbit"));
// 	}

// 	@Bean
// 	public Sender sender(Mono<Connection> connectionMono) {
// 		SenderOptions senderOptions = new SenderOptions()
// 				.connectionMono(connectionMono)
// 				.resourceManagementScheduler(reactor.core.scheduler.Schedulers.boundedElastic());
// 		return RabbitFlux.createSender(senderOptions);
// 	}

// 	@Bean
// 	public Receiver receiver(Mono<Connection> connectionMono) {
// 		ReceiverOptions receiverOptions = new ReceiverOptions()
// 				.connectionMono(connectionMono);
// 		return RabbitFlux.createReceiver(receiverOptions);
// 	}
// }