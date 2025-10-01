package sura.pruebalegoback.r2dbc.patient;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.annotation.Transient;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("patients")
public class PatientEntity {
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String sex;
    private String address;
    private String phone;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}