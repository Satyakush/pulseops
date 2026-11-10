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

    @Test
    void actionFilterRestrictsPageItemsAndTotals() {
        UUID incidentId = UUID.randomUUID();
        repository.save(new IncidentActivity(UUID.randomUUID(), incidentId, IncidentActivityAction.CREATED,
                "operator", "created", java.time.OffsetDateTime.parse("2026-10-13T10:00:00Z")));
        repository.save(new IncidentActivity(UUID.randomUUID(), incidentId, IncidentActivityAction.STATUS_CHANGED,
                "operator", "resolved", java.time.OffsetDateTime.parse("2026-10-13T11:00:00Z")));

        IncidentActivityPage result = repository.findPageByIncidentId(
                incidentId, IncidentActivityAction.STATUS_CHANGED, 0, 10);

        assertThat(result.items()).hasSize(1);
        assertThat(result.items().get(0).action()).isEqualTo(IncidentActivityAction.STATUS_CHANGED);
        assertThat(result.totalElements()).isEqualTo(1);
        assertThat(result.totalPages()).isEqualTo(1);
    }

    @Test
    void paginatesNewestFirstAndReportsTotalMetadata() {
        UUID incidentId = UUID.randomUUID();
        IncidentActivity older = activity(incidentId, "2026-10-13T10:00:00Z");
        IncidentActivity newer = activity(incidentId, "2026-10-13T11:00:00Z");
        repository.save(older);
        repository.save(newer);

        IncidentActivityPage firstPage = repository.findPageByIncidentId(incidentId, 0, 1);
        IncidentActivityPage secondPage = repository.findPageByIncidentId(incidentId, 1, 1);

        assertThat(firstPage.items()).containsExactly(newer);
        assertThat(firstPage.totalElements()).isEqualTo(2);
        assertThat(firstPage.totalPages()).isEqualTo(2);
        assertThat(secondPage.items()).containsExactly(older);
        assertThat(secondPage.page()).isEqualTo(1);
    }

    @Test
    void breaksTimestampTiesByDescendingIdForStablePagination() {
        UUID incidentId = UUID.randomUUID();
        OffsetDateTime sameTime = OffsetDateTime.parse("2026-11-10T10:00:00Z");
        IncidentActivity lowerId = new IncidentActivity(
                UUID.fromString("00000000-0000-0000-0000-000000000001"), incidentId,
                IncidentActivityAction.CREATED, "operator", "lower id", sameTime);
        IncidentActivity higherId = new IncidentActivity(
                UUID.fromString("00000000-0000-0000-0000-000000000002"), incidentId,
                IncidentActivityAction.STATUS_CHANGED, "operator", "higher id", sameTime);
        repository.save(lowerId);
        repository.save(higherId);

        IncidentActivityPage first = repository.findPageByIncidentId(incidentId, 0, 1);
        IncidentActivityPage second = repository.findPageByIncidentId(incidentId, 1, 1);

        assertThat(first.items()).containsExactly(higherId);
        assertThat(second.items()).containsExactly(lowerId);
        assertThat(first.totalElements()).isEqualTo(2);
    }

    private void unusedPlaceholder() {}
    private IncidentActivity activity(UUID incidentId, String timestamp) {
        return new IncidentActivity(UUID.randomUUID(), incidentId, IncidentActivityAction.CREATED,
                "operator", "Incident opened", OffsetDateTime.parse(timestamp));
    }
}
