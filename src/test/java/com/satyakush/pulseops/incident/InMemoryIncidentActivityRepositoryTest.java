package com.satyakush.pulseops.incident;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryIncidentActivityRepositoryTest {
    private final InMemoryIncidentActivityRepository repository = new InMemoryIncidentActivityRepository();

    @Test
    void returnsOnlyTheRequestedIncidentsEntriesNewestFirst() {
        UUID incidentId = UUID.randomUUID();
        IncidentActivity older = activity(incidentId, "2026-10-13T10:00:00Z");
        IncidentActivity newer = activity(incidentId, "2026-10-13T11:00:00Z");
        IncidentActivity otherIncident = activity(UUID.randomUUID(), "2026-10-13T12:00:00Z");
        repository.save(older);
        repository.save(newer);
        repository.save(otherIncident);

        assertThat(repository.findByIncidentId(incidentId)).containsExactly(newer, older);
    }

    @Test
    void returnsAnEmptyListForAnIncidentWithoutActivity() {
        assertThat(repository.findByIncidentId(UUID.randomUUID())).isEmpty();
    }

    private IncidentActivity activity(UUID incidentId, String timestamp) {
        return new IncidentActivity(UUID.randomUUID(), incidentId, IncidentActivityAction.CREATED,
                "operator", "Incident opened", OffsetDateTime.parse(timestamp));
    }
}
