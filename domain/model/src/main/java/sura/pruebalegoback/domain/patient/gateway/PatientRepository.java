package sura.pruebalegoback.domain.patient.gateway;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sura.pruebalegoback.domain.patient.Patient;

public interface PatientRepository {
    Mono<Patient> save(Patient patient);
    Mono<Void> saveAll(Flux<Patient> patients);
    Mono<Patient> findById(Long id);
    Flux<Patient> findAll();
    Mono<Void> deleteById(Long id);
    Mono<Patient> update(Patient patient);
}
