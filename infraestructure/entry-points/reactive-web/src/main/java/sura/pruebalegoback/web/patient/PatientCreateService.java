package sura.pruebalegoback.web.patient;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import sura.pruebalegoback.domain.patient.Patient;
import sura.pruebalegoback.usecase.patient.PatientUseCase;

@RestController
@RequiredArgsConstructor
public class PatientCreateService {

    private final PatientUseCase useCase;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = "/patient", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Patient> create(@RequestBody Patient patient) {
        return useCase.createPatient(patient);
    }
}