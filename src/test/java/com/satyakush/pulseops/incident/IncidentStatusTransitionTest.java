package com.satyakush.pulseops.incident;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import com.satyakush.pulseops.realtime.PulseOpsEventPublisher;
import com.satyakush.pulseops.realtime.PulseOpsEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class IncidentStatusTransitionTest {
    private IncidentRepository repository;
    private IncidentActivityService activityService;
    private PulseOpsEventPublisher eventPublisher;
    private IncidentService service;

    @BeforeEach
    void setUp() {
        repository = mock(IncidentRepository.class);
        activityService = mock(IncidentActivityService.class);
        eventPublisher = mock(PulseOpsEventPublisher.class);
        service = new IncidentService(repository, eventPublisher, activityService);
    }

    @Test
    void allowsOpeningIncidentForInvestigation() {
        Incident incident = incident(IncidentStatus.OPEN);
        when(repository.findById(incident.id())).thenReturn(Optional.of(incident));
        when(repository.save(org.mockito.ArgumentMatchers.any(Incident.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Incident updated = service.updateStatus(incident.id(), IncidentStatus.INVESTIGATING);

        assertThat(updated.status()).isEqualTo(IncidentStatus.INVESTIGATING);
        verify(activityService).record(incident.id(), IncidentActivityAction.STATUS_CHANGED,
                "Incident status changed to INVESTIGATING");
    }

    @Test
    void rejectsNoOpTransitionWithoutSavingOrRecordingActivity() {
        Incident incident = incident(IncidentStatus.OPEN);
        when(repository.findById(incident.id())).thenReturn(Optional.of(incident));

        assertThatThrownBy(() -> service.updateStatus(incident.id(), IncidentStatus.OPEN))
                .isInstanceOf(InvalidIncidentStatusTransitionException.class)
                .hasMessage("Cannot transition incident from OPEN to OPEN");

        verify(repository, never()).save(org.mockito.ArgumentMatchers.any(Incident.class));
        verify(activityService, never()).record(
                org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.anyString());
        verify(eventPublisher, never()).publish(org.mockito.ArgumentMatchers.any(PulseOpsEvent.class));
    }

    @Test
    void rejectsReopeningResolvedIncidentDirectlyToOpen() {
        Incident incident = incident(IncidentStatus.RESOLVED);
        when(repository.findById(incident.id())).thenReturn(Optional.of(incident));

        assertThatThrownBy(() -> service.updateStatus(incident.id(), IncidentStatus.OPEN))
                .isInstanceOf(InvalidIncidentStatusTransitionException.class)
                .hasMessage("Cannot transition incident from RESOLVED to OPEN");

        verify(repository, never()).save(org.mockito.ArgumentMatchers.any(Incident.class));
    }

    @Test
    void rejectsNullRequestedStatusWithoutSaving() {
        Incident incident = incident(IncidentStatus.OPEN);
        when(repository.findById(incident.id())).thenReturn(Optional.of(incident));

        assertThatThrownBy(() -> service.updateStatus(incident.id(), null))
                .isInstanceOf(InvalidIncidentStatusTransitionException.class)
                .hasMessage("Cannot transition incident from OPEN to null");

        verify(repository, never()).save(org.mockito.ArgumentMatchers.any(Incident.class));
    }

    @Test
    void allowsResolvedIncidentToReturnToInvestigation() {
        Incident incident = incident(IncidentStatus.RESOLVED);
        when(repository.findById(incident.id())).thenReturn(Optional.of(incident));
        when(repository.save(org.mockito.ArgumentMatchers.any(Incident.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Incident updated = service.updateStatus(incident.id(), IncidentStatus.INVESTIGATING);

        assertThat(updated.status()).isEqualTo(IncidentStatus.INVESTIGATING);
    }

    @ParameterizedTest
    @CsvSource({
            "OPEN,OPEN,false",
            "OPEN,INVESTIGATING,true",
            "OPEN,RESOLVED,true",
            "INVESTIGATING,OPEN,true",
            "INVESTIGATING,INVESTIGATING,false",
            "INVESTIGATING,RESOLVED,true",
            "RESOLVED,OPEN,false",
            "RESOLVED,INVESTIGATING,true",
            "RESOLVED,RESOLVED,false"
    })
    void lifecycleMatrixExplicitlyDefinesEveryStatusPair(
            IncidentStatus current, IncidentStatus requested, boolean allowed) {
        assertThat(IncidentService.isAllowedTransition(current, requested)).isEqualTo(allowed);
    }

    private Incident incident(IncidentStatus status) {
        return new Incident(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "API latency",
                "Latency increased",
                IncidentSeverity.HIGH,
                status,
                OffsetDateTime.parse("2026-10-16T10:00:00Z"));
    }
}
