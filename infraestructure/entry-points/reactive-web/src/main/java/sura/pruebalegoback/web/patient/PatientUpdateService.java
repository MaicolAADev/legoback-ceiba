package sura.pruebalegoback.web.patient;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import sura.pruebalegoback.domain.patient.Patient;
import sura.pruebalegoback.usecase.patient.PatientUseCase;

@RestController
@RequiredArgsConstructor
public class PatientUpdateService {

    private final PatientUseCase useCase;

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = "/patient", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Patient> update(@RequestBody Patient patient) {
        return useCase.updatePatient(patient);
    }
}