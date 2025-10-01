package sura.pruebalegoback.domain.patient;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PatientTest {

    @Test
    void builderAndGetters() {
        LocalDateTime now = LocalDateTime.now();
        Patient patient = Patient.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 1, 1))
                .sex("M")
                .address("123 Main St")
                .phone("1234567890")
                .email("john.doe@example.com")
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertEquals(1L, patient.getId());
        assertEquals("John", patient.getFirstName());
        assertEquals("Doe", patient.getLastName());
        assertEquals(LocalDate.of(1990, 1, 1), patient.getBirthDate());
        assertEquals("M", patient.getSex());
        assertEquals("123 Main St", patient.getAddress());
        assertEquals("1234567890", patient.getPhone());
        assertEquals("john.doe@example.com", patient.getEmail());
        assertEquals(now, patient.getCreatedAt());
        assertEquals(now, patient.getUpdatedAt());
    }

    @Test
    void equalsAndHashCode() {
        LocalDateTime now = LocalDateTime.now();
        Patient p1 = Patient.builder().id(1L).firstName("A").createdAt(now).updatedAt(now).build();
        Patient p2 = Patient.builder().id(1L).firstName("A").createdAt(now).updatedAt(now).build();
        Patient p3 = Patient.builder().id(2L).firstName("B").createdAt(now).updatedAt(now).build();

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
        assertNotEquals(p1, p3);
    }

    @Test
    void toBuilderCreatesCopy() {
        Patient p1 = Patient.builder().id(1L).firstName("A").build();
        Patient p2 = p1.toBuilder().firstName("B").build();
        assertEquals(1L, p2.getId());
        assertEquals("B", p2.getFirstName());
        assertNotEquals(p1, p2);
    }
}