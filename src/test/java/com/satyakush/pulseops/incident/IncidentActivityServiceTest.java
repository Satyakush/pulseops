package com.satyakush.pulseops.incident;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

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
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> service.record(UUID.randomUUID(), IncidentActivityAction.CREATED, "  "))
                .withMessage("activity summary must not be blank");
    }

    @Test
    void rejectsActivitySummaryLongerThanStorageContract() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> service.record(UUID.randomUUID(), IncidentActivityAction.CREATED, "x".repeat(501)))
                .withMessage("activity summary must be at most 500 characters");
    }

    @Test
    void rejectsMissingIncidentAndAction() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> service.record(null, IncidentActivityAction.CREATED, "created"));
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> service.record(UUID.randomUUID(), null, "created"));
    }
    @Test
    void fallsBackToSystemWhenActorIsMissingOrBlank() {
        IncidentActivityService missingActorService =
                new IncidentActivityService(repository, () -> null);
        IncidentActivityService blankActorService =
                new IncidentActivityService(repository, () -> "   ");

        IncidentActivity missingActor = missingActorService.record(
                UUID.randomUUID(), IncidentActivityAction.CREATED, "created");
        IncidentActivity blankActor = blankActorService.record(
                UUID.randomUUID(), IncidentActivityAction.CREATED, "created");

        assertThat(missingActor.actor()).isEqualTo("system");
        assertThat(blankActor.actor()).isEqualTo("system");
    }

    @Test
    void truncatesActorToStorageLimit() {
        String longActor = "a".repeat(121);
        IncidentActivityService longActorService =
                new IncidentActivityService(repository, () -> longActor);

        IncidentActivity saved = longActorService.record(
                UUID.randomUUID(), IncidentActivityAction.CREATED, "created");

        assertThat(saved.actor()).hasSize(120).isEqualTo("a".repeat(120));
    }

    @Test
    void acceptsActivitySummaryAtMaximumStorageLength() {
        String summary = "x".repeat(500);

        IncidentActivity saved = service.record(
                UUID.randomUUID(), IncidentActivityAction.CREATED, summary);

        assertThat(saved.summary()).hasSize(500).isEqualTo(summary);
    }

    @Test
    void rejectsNegativeActivityPage() {
        assertThatExceptionOfType(InvalidIncidentPageRequestException.class)
                .isThrownBy(() -> service.findPageForIncident(UUID.randomUUID(), -1, 20))
                .withMessage("page must be zero or greater");
    }

    @Test
    void rejectsActivityPageSizesOutsideSupportedRange() {
        UUID incidentId = UUID.randomUUID();

        assertThatExceptionOfType(InvalidIncidentPageRequestException.class)
                .isThrownBy(() -> service.findPageForIncident(incidentId, 0, 0))
                .withMessage("size must be between 1 and 100");
        assertThatExceptionOfType(InvalidIncidentPageRequestException.class)
                .isThrownBy(() -> service.findPageForIncident(incidentId, 0, 101))
                .withMessage("size must be between 1 and 100");
    }

    @Test
    void allowsMaximumActivityPageSize() {
        IncidentActivityPage page = service.findPageForIncident(UUID.randomUUID(), 0, 100);

        assertThat(page.size()).isEqualTo(100);
        assertThat(page.items()).isEmpty();
    }

}
