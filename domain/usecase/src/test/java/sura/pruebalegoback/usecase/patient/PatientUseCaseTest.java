package sura.pruebalegoback.usecase.patient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import sura.pruebalegoback.domain.common.ex.BusinessException;
import sura.pruebalegoback.domain.patient.Patient;
import sura.pruebalegoback.domain.patient.gateway.PatientRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatientUseCaseTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientUseCase patientUseCase;

    private Patient patient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        patient = Patient.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 1, 1))
                .sex("M")
                .address("123 Main St")
                .phone("1234567890")
                .email("john.doe@example.com")
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now().minusHours(1))
                .build();
    }

    @Test
    void createPatient_success() {
        when(patientRepository.save(any(Patient.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));
        StepVerifier.create(patientUseCase.createPatient(patient))
                .assertNext(p -> {
                    assertEquals(patient.getId(), p.getId());
                    assertEquals(patient.getFirstName(), p.getFirstName());
                    assertEquals(patient.getLastName(), p.getLastName());
                    assertEquals(patient.getBirthDate(), p.getBirthDate());
                    assertEquals(patient.getSex(), p.getSex());
                    assertEquals(patient.getAddress(), p.getAddress());
                    assertEquals(patient.getPhone(), p.getPhone());
                    assertEquals(patient.getEmail(), p.getEmail());
                })
                .verifyComplete();
        verify(patientRepository).save(any(Patient.class));
    }

    @Test
    void createPatient_invalidFirstName() {
        Patient invalid = patient.toBuilder().firstName("").build();
        StepVerifier.create(patientUseCase.createPatient(invalid))
                .expectErrorSatisfies(e -> assertTrue(e instanceof BusinessException))
                .verify();
    verify(patientRepository, never()).save(patient);
    }

    @Test
    void updatePatient_success() {
        when(patientRepository.findById(patient.getId())).thenReturn(Mono.just(patient));
        when(patientRepository.update(any(Patient.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));
        StepVerifier.create(patientUseCase.updatePatient(patient))
                .assertNext(p -> {
                    assertEquals(patient.getId(), p.getId());
                    assertEquals(patient.getFirstName(), p.getFirstName());
                    assertEquals(patient.getLastName(), p.getLastName());
                    assertEquals(patient.getBirthDate(), p.getBirthDate());
                    assertEquals(patient.getSex(), p.getSex());
                    assertEquals(patient.getAddress(), p.getAddress());
                    assertEquals(patient.getPhone(), p.getPhone());
                    assertEquals(patient.getEmail(), p.getEmail());
                })
                .verifyComplete();
        verify(patientRepository).update(any(Patient.class));
    }

    @Test
    void updatePatient_notFound() {
        when(patientRepository.findById(patient.getId())).thenReturn(Mono.empty());
        StepVerifier.create(patientUseCase.updatePatient(patient))
                .expectErrorSatisfies(e -> {
                    assertTrue(e instanceof BusinessException);
                    assertEquals(BusinessException.Type.PATIENT_NOT_FOUND.name(), ((BusinessException) e).getCode());
                })
                .verify();
    }

    @Test
    void getPatientById_success() {
        when(patientRepository.findById(1L)).thenReturn(Mono.just(patient));
        StepVerifier.create(patientUseCase.getPatientById(1L))
                .expectNext(patient)
                .verifyComplete();
    }

    @Test
    void getPatientById_notFound() {
        when(patientRepository.findById(1L)).thenReturn(Mono.empty());
        StepVerifier.create(patientUseCase.getPatientById(1L))
                .expectErrorSatisfies(e -> assertTrue(e instanceof BusinessException))
                .verify();
    }

    @Test
    void getAllPatients_success() {
        when(patientRepository.findAll()).thenReturn(Flux.just(patient));
        StepVerifier.create(patientUseCase.getAllPatients())
                .expectNext(patient)
                .verifyComplete();
    }

    @Test
    void deletePatientById_success() {
        when(patientRepository.findById(1L)).thenReturn(Mono.just(patient));
        when(patientRepository.deleteById(1L)).thenReturn(Mono.empty());
        StepVerifier.create(patientUseCase.deletePatientById(1L))
                .verifyComplete();
        verify(patientRepository).deleteById(1L);
    }

    @Test
    void deletePatientById_notFound() {
        when(patientRepository.findById(1L)).thenReturn(Mono.empty());
        StepVerifier.create(patientUseCase.deletePatientById(1L))
                .expectErrorSatisfies(e -> assertTrue(e instanceof BusinessException))
                .verify();
    verify(patientRepository, never()).deleteById(1L);
    }

    @Test
    void exportPatientsToExcel_success() {
        when(patientRepository.findAll()).thenReturn(Flux.just(patient));
        Supplier<byte[]> excelSupplier = () -> new byte[]{1, 2, 3};
        StepVerifier.create(patientUseCase.exportPatientsToExcel(excelSupplier))
                .expectNextMatches(arr -> arr.length == 3 && arr[0] == 1)
                .verifyComplete();
    }

    @Test
    void exportPatientsToExcel_empty() {
        when(patientRepository.findAll()).thenReturn(Flux.empty());
        Supplier<byte[]> excelSupplier = () -> new byte[]{};
        StepVerifier.create(patientUseCase.exportPatientsToExcel(excelSupplier))
                .expectNextMatches(arr -> arr.length == 0)
                .verifyComplete();
    }
}