package sura.pruebalegoback.r2dbc.patient;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientR2dbcRepository extends ReactiveCrudRepository<PatientEntity, Long> {
    // Métodos adicionales si se requieren
}