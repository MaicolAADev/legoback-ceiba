// package sura.pruebalegoback.reactive;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import io.projectreactor.rabbitmq.ConsumeOptions;
// import io.projectreactor.rabbitmq.Receiver;
// import jakarta.annotation.PostConstruct;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.stereotype.Component;
// import reactor.core.Disposable;
// import reactor.core.publisher.Mono;
// import reactor.rabbitmq.AcknowledgableDelivery;
// import sura.pruebalegoback.domain.patient.event.PatientCreatedEvent;

// @Slf4j
// @Component
// @RequiredArgsConstructor
// public class PatientEventListener {
    
//     private final Receiver receiver;
//     private final ObjectMapper objectMapper;
//     private Disposable subscription;
    
//     private static final String QUEUE = "patient.created.queue";
    
//     @PostConstruct
//     public void startListening() {
//         ConsumeOptions options = new ConsumeOptions()
//                 .qos(10); // prefetch count
        
//         subscription = receiver.consumeManualAck(QUEUE, options)
//                 .flatMap(this::handleMessage)
//                 .subscribe(
//                         v -> {},
//                         error -> {
//                             log.error("Error in message consumption", error);
//                             // Restart subscription after error
//                             startListening();
//                         }
//                 );
        
//         log.info("Started listening to queue: {}", QUEUE);
//     }
    
//     private Mono<Void> handleMessage(AcknowledgableDelivery delivery) {
//         return Mono.fromCallable(() -> {
//                     byte[] body = delivery.getBody();
//                     return objectMapper.readValue(body, PatientCreatedEvent.class);
//                 })
//                 .doOnNext(event -> log.info("Received event: {}", event))
//                 .flatMap(this::processEvent)
//                 .doOnSuccess(v -> delivery.ack())
//                 .doOnError(error -> {
//                     log.error("Error processing message", error);
//                     delivery.nack(false); // nack without requeue
//                 })
//                 .onErrorResume(error -> Mono.empty())
//                 .then();
//     }
    
//     private Mono<Void> processEvent(PatientCreatedEvent event) {
//         // Aquí implementas tu lógica de negocio
//         return Mono.fromRunnable(() -> {
//             log.info("Processing patient created event for patient: {}", event);
//             // Tu lógica aquí
//         });
//     }
    
//     public void stopListening() {
//         if (subscription != null && !subscription.isDisposed()) {
//             subscription.dispose();
//             log.info("Stopped listening to queue: {}", QUEUE);
//         }
//     }
// }