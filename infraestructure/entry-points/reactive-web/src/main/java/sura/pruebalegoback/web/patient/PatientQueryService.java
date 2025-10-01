package sura.pruebalegoback.web.patient;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sura.pruebalegoback.domain.patient.Patient;
import sura.pruebalegoback.usecase.patient.PatientUseCase;

@RestController
@RequestMapping(value = "/patient", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class PatientQueryService {

    private final PatientUseCase useCase;

    @GetMapping(path = "/{id}")
    public Mono<Patient> getById(@PathVariable("id") Long id) {
        return useCase.getPatientById(id);
    }

    @GetMapping
    public Flux<Patient> getAll() {
        return useCase.getAllPatients();
    }

    @DeleteMapping(path = "/{id}")
    public Mono<Void> deleteById(@PathVariable("id") Long id) {
        return useCase.deletePatientById(id);
    }
}