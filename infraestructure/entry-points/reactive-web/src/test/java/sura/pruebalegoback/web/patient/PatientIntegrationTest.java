package sura.pruebalegoback.web.patient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sura.pruebalegoback.domain.patient.Patient;
import sura.pruebalegoback.reactive.PatientEventPublisher;
import sura.pruebalegoback.usecase.patient.PatientUseCase;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest({PatientQueryService.class, PatientCreateService.class, PatientUpdateService.class})
class PatientIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private PatientUseCase patientUseCase;

    @MockBean
    private PatientExcelExport patientExcelExport;

    @MockBean
    private PatientEventPublisher patientEventPublisher;

    private Patient patient;

    @BeforeEach
    void setUp() {
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
    void testUpdatePatient() {
        when(patientUseCase.updatePatient(any())).thenReturn(Mono.just(patient));
        webTestClient.put().uri("/patient")
                .bodyValue(patient)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Patient.class)
                .isEqualTo(patient);
    }

    @Test
    void testGetPatientById() {
        when(patientUseCase.getPatientById(1L)).thenReturn(Mono.just(patient));
        webTestClient.get().uri("/patient/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Patient.class)
                .isEqualTo(patient);
    }

    @Test
    void testGetAllPatients() {
        when(patientUseCase.getAllPatients()).thenReturn(Flux.just(patient));
        webTestClient.get().uri("/patient")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Patient.class)
                .hasSize(1)
                .contains(patient);
    }

    @Test
    void testDeletePatientById() {
        when(patientUseCase.deletePatientById(1L)).thenReturn(Mono.empty());
        webTestClient.delete().uri("/patient/1")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testExportPatientsToExcel() {
        byte[] excelBytes = new byte[]{1,2,3};
        when(patientExcelExport.buildExcel()).thenReturn(Mono.just(excelBytes));
        webTestClient.get().uri("/patient/export/excel")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Disposition", "attachment; filename=pacientes.xlsx")
                .expectBody(byte[].class)
                .isEqualTo(excelBytes);
    }
}
