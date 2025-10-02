package sura.pruebalegoback.web.patient;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import sura.pruebalegoback.domain.patient.Patient;
import sura.pruebalegoback.domain.patient.event.PatientCreatedEvent;
import sura.pruebalegoback.reactive.PatientEventPublisher;
import sura.pruebalegoback.usecase.patient.PatientUseCase;

@RestController
@RequiredArgsConstructor
public class PatientCreateService {

    private final PatientUseCase useCase;
    private final PatientEventPublisher patientEventPublisher;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = "/patient", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Patient> create(@RequestBody Patient patient) {
        return useCase.createPatient(patient)
            .doOnSuccess(savedPatient -> {
                patientEventPublisher.publish(PatientCreatedEvent.builder().patient(savedPatient).build())
                    .subscribe();
            });
    }
}