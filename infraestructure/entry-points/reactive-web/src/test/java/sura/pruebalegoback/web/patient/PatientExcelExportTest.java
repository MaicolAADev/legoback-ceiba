
package sura.pruebalegoback.web.patient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sura.pruebalegoback.domain.patient.Patient;
import sura.pruebalegoback.usecase.patient.PatientUseCase;
import sura.pruebalegoback.web.services.ExcelExportService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatientExcelExportTest {
    private PatientUseCase useCase;
    private ExcelExportService excelExportService;
    private PatientExcelExport patientExcelExport;

    @BeforeEach
    void setUp() {
        useCase = mock(PatientUseCase.class);
        excelExportService = mock(ExcelExportService.class);
        patientExcelExport = new PatientExcelExport(useCase, excelExportService);
    }

    @Test
    void testBuildExcelWithPatients() {
        Patient patient = Patient.builder()
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
        when(useCase.getAllPatients()).thenReturn(Flux.just(patient));
        List<Patient> patientList = List.of(patient);
        when(excelExportService.generateExcel(patientList, PatientExcelExport.COLUMNS, PatientExcelExport.ROW_MAPPER, "Pacientes")).thenReturn(new byte[]{1,2,3});
        Mono<byte[]> result = patientExcelExport.buildExcel();
        assertArrayEquals(new byte[]{1,2,3}, result.block());
    }

    @Test
    void testBuildExcelWithEmptyList() {
        when(useCase.getAllPatients()).thenReturn(Flux.empty());
        List<Patient> emptyList = List.of();
        when(excelExportService.generateExcel(emptyList, PatientExcelExport.COLUMNS, PatientExcelExport.ROW_MAPPER, "Pacientes")).thenReturn(new byte[]{4,5,6});
        Mono<byte[]> result = patientExcelExport.buildExcel();
        assertArrayEquals(new byte[]{4,5,6}, result.block());
    }

    @Test
    void testBuildExcelWithNullFields() {
        Patient patient = Patient.builder()
                .id(null)
                .firstName(null)
                .lastName(null)
                .birthDate(null)
                .sex(null)
                .address(null)
                .phone(null)
                .email(null)
                .createdAt(null)
                .updatedAt(null)
                .build();
        when(useCase.getAllPatients()).thenReturn(Flux.just(patient));
        List<Patient> patientList = List.of(patient);
        when(excelExportService.generateExcel(patientList, PatientExcelExport.COLUMNS, PatientExcelExport.ROW_MAPPER, "Pacientes")).thenReturn(new byte[]{7,8,9});
        Mono<byte[]> result = patientExcelExport.buildExcel();
        assertArrayEquals(new byte[]{7,8,9}, result.block());
    }
}