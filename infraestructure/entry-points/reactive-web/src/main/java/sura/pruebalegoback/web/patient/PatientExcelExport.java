package sura.pruebalegoback.web.patient;

import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;
import sura.pruebalegoback.domain.patient.Patient;
import sura.pruebalegoback.usecase.patient.PatientUseCase;
import sura.pruebalegoback.web.services.ExcelExportService;

import java.util.List;
import java.util.function.Function;

@Component
public class PatientExcelExport {
	public static final List<String> COLUMNS = List.of("ID", "Nombre", "Apellido", "Fecha Nacimiento", "Sexo", "Dirección", "Teléfono", "Email", "Creado", "Actualizado");
	public static final Function<Patient, List<Object>> ROW_MAPPER = p -> List.of(
		p.getId() != null ? p.getId() : 0,
		p.getFirstName() != null ? p.getFirstName() : "",
		p.getLastName() != null ? p.getLastName() : "",
		p.getBirthDate() != null ? p.getBirthDate().toString() : "",
		p.getSex() != null ? p.getSex() : "",
		p.getAddress() != null ? p.getAddress() : "",
		p.getPhone() != null ? p.getPhone() : "",
		p.getEmail() != null ? p.getEmail() : "",
		p.getCreatedAt() != null ? p.getCreatedAt().toString() : "",
		p.getUpdatedAt() != null ? p.getUpdatedAt().toString() : ""
	);

	private final PatientUseCase useCase;
	private final ExcelExportService excelExportService;

	public PatientExcelExport(PatientUseCase useCase, ExcelExportService excelExportService) {
		this.useCase = useCase;
		this.excelExportService = excelExportService;
	}

	public Mono<byte[]> buildExcel() {
		return useCase.getAllPatients()
			.collectList()
			.map(patients -> excelExportService.generateExcel(patients, COLUMNS, ROW_MAPPER, "Pacientes"));
	}
}
