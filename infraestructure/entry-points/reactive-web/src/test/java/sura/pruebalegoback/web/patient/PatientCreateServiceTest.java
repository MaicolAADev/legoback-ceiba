package sura.pruebalegoback.web.patient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import reactor.core.publisher.Mono;
import sura.pruebalegoback.domain.patient.Patient;
import sura.pruebalegoback.domain.patient.event.PatientCreatedEvent;
import sura.pruebalegoback.reactive.PatientEventPublisher;
import sura.pruebalegoback.usecase.patient.PatientUseCase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PatientCreateServiceTest {
    private PatientUseCase useCase;
    private PatientEventPublisher eventPublisher;
    private PatientCreateService service;

    @BeforeEach
    void setUp() {
        useCase = mock(PatientUseCase.class);
        eventPublisher = mock(PatientEventPublisher.class);
        service = new PatientCreateService(useCase, eventPublisher);
    }

    @Test
    void testCreatePatientSuccessPublishesEvent() {
        Patient patient = Patient.builder().id(1L).firstName("John").build();
        when(useCase.createPatient(any())).thenReturn(Mono.just(patient));
        when(eventPublisher.publish(any())).thenReturn(Mono.empty());

        Mono<Patient> result = service.create(patient);
        Patient saved = result.block();
        assertEquals(patient, saved);

        ArgumentCaptor<PatientCreatedEvent> captor = ArgumentCaptor.forClass(PatientCreatedEvent.class);
        verify(eventPublisher, times(1)).publish(captor.capture());
        assertEquals(patient, captor.getValue().getPatient());
    }

    @Test
    void testCreatePatientErrorDoesNotPublishEvent() {
        Patient patient = Patient.builder().id(2L).firstName("Jane").build();
        when(useCase.createPatient(any())).thenReturn(Mono.error(new RuntimeException("fail")));

        Mono<Patient> result = service.create(patient);
        assertThrows(RuntimeException.class, result::block);
        verify(eventPublisher, never()).publish(any());
    }

    @Test
    void testCreatePatientEventPublisherErrorIsIgnored() {
        Patient patient = Patient.builder().id(3L).firstName("Foo").build();
        when(useCase.createPatient(any())).thenReturn(Mono.just(patient));
        when(eventPublisher.publish(any())).thenReturn(Mono.error(new RuntimeException("event fail")));

        Mono<Patient> result = service.create(patient);
        Patient saved = result.block();
        assertEquals(patient, saved);
        verify(eventPublisher, times(1)).publish(any());
    }
}