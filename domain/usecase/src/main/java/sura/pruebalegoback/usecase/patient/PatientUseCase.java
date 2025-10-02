package sura.pruebalegoback.usecase.patient;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sura.pruebalegoback.domain.common.ex.BusinessException;
import sura.pruebalegoback.domain.patient.Patient;
import sura.pruebalegoback.domain.patient.gateway.PatientRepository;

import reactor.core.scheduler.Schedulers;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class PatientUseCase {

    private final PatientRepository patientRepository;


    public Mono<Patient> createPatient(Patient patient) {
        if (patient.getFirstName() == null || patient.getFirstName().isBlank()) {
            return Mono.error(new BusinessException(BusinessException.Type.INVALID_TODO_INITIAL_DATA));
        }
        return patientRepository.save(setDates(patient));
    }

    public Mono<Patient> updatePatient(Patient patient) {
        return patientRepository.findById(patient.getId())
            .switchIfEmpty(Mono.error(new BusinessException(BusinessException.Type.PATIENT_NOT_FOUND)))
            .flatMap(existingPatient -> patientRepository.update(setDates(patient, existingPatient.getCreatedAt())));
    }

    public Mono<Patient> getPatientById(Long id) {
        return patientRepository.findById(id)
            .switchIfEmpty(Mono.error(new BusinessException(BusinessException.Type.PATIENT_NOT_FOUND)));
    }

    public Flux<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Mono<Void> deletePatientById(Long id) {
        return patientRepository.findById(id)
            .switchIfEmpty(Mono.error(new BusinessException(BusinessException.Type.PATIENT_NOT_FOUND)))
            .flatMap(patient -> patientRepository.deleteById(id));
    }

    private Patient setDates(Patient patient) {
        return setDates(patient, patient.getId() == null ? java.time.LocalDateTime.now() : patient.getCreatedAt());
    }

    private Patient setDates(Patient patient, java.time.LocalDateTime createdAt) {
        return patient.toBuilder()
            .createdAt(createdAt)
            .updatedAt(java.time.LocalDateTime.now())
            .build();
    }

    public Flux<byte[]> exportPatientsToExcel(Supplier<byte[]> excelGenerator) {
        return getAllPatients()
            .collectList()
            .publishOn(Schedulers.boundedElastic())
            .map(patients -> excelGenerator.get())
            .flatMapMany(bytes -> Flux.just(bytes));

    }
}