package sura.pruebalegoback.domain.patient.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sura.pruebalegoback.domain.common.Event;
import sura.pruebalegoback.domain.patient.Patient;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientCreatedEvent implements Event {
    public static final String EVENT_NAME = "patient.created";
    private Patient patient;
    @Override
    public String name() {
        return EVENT_NAME;
    }
}
