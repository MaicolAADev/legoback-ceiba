package sura.pruebalegoback.domain.patient;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Patient {
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
