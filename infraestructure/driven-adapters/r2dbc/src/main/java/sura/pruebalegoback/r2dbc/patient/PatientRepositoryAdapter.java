package sura.pruebalegoback.r2dbc.patient;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sura.pruebalegoback.domain.patient.Patient;
import sura.pruebalegoback.domain.patient.gateway.PatientRepository;

@Component
@RequiredArgsConstructor
public class PatientRepositoryAdapter implements PatientRepository {

    private final PatientR2dbcRepository repository;

    @Override
    public Mono<Patient> save(Patient patient) {
        return repository.save(toEntity(patient)).map(this::toDomain);
    }

    @Override
    public Mono<Void> saveAll(Flux<Patient> patients) {
        return repository.saveAll(patients.map(this::toEntity)).then();
    }

    @Override
    public Mono<Patient> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Flux<Patient> findAll() {
        return repository.findAll().map(this::toDomain);
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return repository.deleteById(id);
    }

    @Override
    public Mono<Patient> update(Patient patient) {
    return repository.existsById(patient.getId())
            .flatMap(exists -> exists ? repository.save(toEntity(patient)).map(this::toDomain) : Mono.empty());
    }

    private PatientEntity toEntity(Patient patient) {
        return PatientEntity.builder()
                .id(patient.getId())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .birthDate(patient.getBirthDate())
                .sex(patient.getSex())
                .address(patient.getAddress())
                .phone(patient.getPhone())
                .email(patient.getEmail())
                .createdAt(patient.getCreatedAt())
                .updatedAt(patient.getUpdatedAt())
                .build();
    }

    private Patient toDomain(PatientEntity entity) {
        return Patient.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .birthDate(entity.getBirthDate())
                .sex(entity.getSex())
                .address(entity.getAddress())
                .phone(entity.getPhone())
                .email(entity.getEmail())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}