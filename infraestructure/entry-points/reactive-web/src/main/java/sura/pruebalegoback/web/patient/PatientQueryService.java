package sura.pruebalegoback.web.patient;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sura.pruebalegoback.domain.patient.Patient;
import sura.pruebalegoback.usecase.patient.PatientUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;

import java.util.function.Supplier;

@RestController
@RequestMapping(value = "/patient", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class PatientQueryService {

    private final PatientUseCase useCase;

    private final PatientExcelExport patientExcelExport;

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

    @GetMapping(value = "/export/excel", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Mono<ResponseEntity<Flux<DataBuffer>>> exportToExcel() {
        Supplier<byte[]> excelSupplier = () -> {
            try {
                return patientExcelExport.buildExcel();
            } catch (Exception e) {
                log.error("Error generando Excel", e);
                throw new RuntimeException("Error generando Excel", e);
            }
        };
        Flux<DataBuffer> excelFlux = useCase.exportPatientsToExcel(excelSupplier)
                .map(bytes -> new DefaultDataBufferFactory().wrap(bytes));
        return Mono.just(ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=pacientes.xlsx")
                .body(excelFlux));
    }
}