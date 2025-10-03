package sura.pruebalegoback.web.patient;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import sura.pruebalegoback.domain.patient.Patient;
import sura.pruebalegoback.web.ErrorHandler;
import sura.pruebalegoback.domain.patient.event.PatientCreatedEvent;
import sura.pruebalegoback.reactive.PatientEventPublisher;
import sura.pruebalegoback.usecase.patient.PatientUseCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequiredArgsConstructor
public class PatientCreateService {

    private final PatientUseCase useCase;
    private final PatientEventPublisher patientEventPublisher;
    private static final Logger log = LoggerFactory.getLogger(PatientCreateService.class);

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = "/patient", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Patient> create(@RequestBody Patient patient) {
        log.info("Creando Paciente");
        return useCase.createPatient(patient)
            .doOnSuccess(savedPatient -> {
                log.info("Patiento creado con id: " + savedPatient.getId());
                patientEventPublisher.publish(PatientCreatedEvent.builder().patient(savedPatient).build())
                    .subscribe();
                })
                .onErrorResume(ErrorHandler::handle);
    }
}