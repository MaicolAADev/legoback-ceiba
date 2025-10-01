package sura.pruebalegoback.web.patient;

import org.springframework.stereotype.Component;
import sura.pruebalegoback.domain.patient.Patient;
import sura.pruebalegoback.usecase.patient.PatientUseCase;
import sura.pruebalegoback.web.services.ExcelExportService;

import java.util.List;
import java.util.function.Function;

@Component
public class PatientExcelExport {

	private final PatientUseCase useCase;
	private final ExcelExportService excelExportService;

	public PatientExcelExport(PatientUseCase useCase, ExcelExportService excelExportService) {
		this.useCase = useCase;
		this.excelExportService = excelExportService;
	}

	public byte[] buildExcel() {
		List<String> columns = List.of("ID", "Nombre", "Apellido", "Fecha Nacimiento", "Sexo", "Dirección", "Teléfono", "Email", "Creado", "Actualizado");
		Function<Patient, List<Object>> rowMapper = p -> List.of(
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
		List<Patient> patients = useCase.getAllPatients().collectList().block();
		return excelExportService.generateExcel(patients, columns, rowMapper, "Pacientes");
	}
}
