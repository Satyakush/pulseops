package com.satyakush.pulseops.incident;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IncidentActivityServiceTest {
    private final InMemoryIncidentActivityRepository repository = new InMemoryIncidentActivityRepository();
    private final IncidentActivityService service = new IncidentActivityService(repository, () -> "operator");

    @Test
    void recordsActorTimestampAndNormalizedSummary() {
        UUID incidentId = UUID.randomUUID();

        IncidentActivity saved = service.record(incidentId, IncidentActivityAction.CREATED, "  Incident opened  ");

        assertThat(saved.incidentId()).isEqualTo(incidentId);
        assertThat(saved.action()).isEqualTo(IncidentActivityAction.CREATED);
        assertThat(saved.actor()).isEqualTo("operator");
        assertThat(saved.summary()).isEqualTo("Incident opened");
        assertThat(saved.id()).isNotNull();
        assertThat(saved.occurredAt()).isNotNull();
        assertThat(repository.findByIncidentId(incidentId)).containsExactly(saved);
    }

    @Test
    void rejectsBlankActivitySummary() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.record(UUID.randomUUID(), IncidentActivityAction.CREATED, "  "))
                .withMessage("activity summary must not be blank");
    }

    @Test
    void rejectsActivitySummaryLongerThanStorageContract() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.record(UUID.randomUUID(), IncidentActivityAction.CREATED, "x".repeat(501)))
                .withMessage("activity summary must be at most 500 characters");
    }

    @Test
    void rejectsMissingIncidentAndAction() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.record(null, IncidentActivityAction.CREATED, "created"));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.record(UUID.randomUUID(), null, "created"));
    }
    @Test
    void fallsBackToSystemActorWhenProviderReturnsNullOrBlank() {
        IncidentActivityService nullActorService = new IncidentActivityService(repository, () -> null);
        IncidentActivityService blankActorService = new IncidentActivityService(repository, () -> "   ");

        assertThat(nullActorService.record(UUID.randomUUID(), IncidentActivityAction.CREATED, "Created").actor())
                .isEqualTo("system");
        assertThat(blankActorService.record(UUID.randomUUID(), IncidentActivityAction.CREATED, "Created").actor())
                .isEqualTo("system");
    }

    @Test
    void truncatesActorNamesToStorageLimit() {
        IncidentActivityService longActorService = new IncidentActivityService(repository, () -> "a".repeat(150));

        IncidentActivity saved = longActorService.record(UUID.randomUUID(), IncidentActivityAction.CREATED, "Created");

        assertThat(saved.actor()).hasSize(120).isEqualTo("a".repeat(120));
    }

    @Test
    void acceptsSummaryAtMaximumLength() {
        IncidentActivity saved = service.record(UUID.randomUUID(), IncidentActivityAction.CREATED, "x".repeat(500));

        assertThat(saved.summary()).hasSize(500);
    }

    @Test
    void rejectsInvalidPaginationBounds() {
        assertThatThrownBy(() -> service.findPageForIncident(UUID.randomUUID(), -1, 20))
                .isInstanceOf(InvalidIncidentPageRequestException.class)
                .hasMessage("page must be zero or greater");
        assertThatThrownBy(() -> service.findPageForIncident(UUID.randomUUID(), 0, 101))
                .isInstanceOf(InvalidIncidentPageRequestException.class)
                .hasMessage("size must be between 1 and 100");
    }

}
