package com.satyakush.pulseops.incident;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class IncidentActivityService {
    private final IncidentActivityRepository repository;
    private final IncidentActorProvider actorProvider;

    public IncidentActivityService(IncidentActivityRepository repository, IncidentActorProvider actorProvider) {
        this.repository = repository;
        this.actorProvider = actorProvider;
    }

    public IncidentActivity record(UUID incidentId, IncidentActivityAction action, String summary) {
        if (incidentId == null) {
            throw new IllegalArgumentException("incidentId is required");
        }
        if (action == null) {
            throw new IllegalArgumentException("activity action is required");
        }
        String normalizedSummary = summary == null ? "" : summary.trim();
        if (normalizedSummary.isEmpty()) {
            throw new IllegalArgumentException("activity summary must not be blank");
        }
        if (normalizedSummary.length() > 500) {
            throw new IllegalArgumentException("activity summary must be at most 500 characters");
        }
        String actor = actorProvider.currentActor();
        if (actor == null || actor.isBlank()) {
            actor = "system";
        }
        if (actor.length() > 120) {
            actor = actor.substring(0, 120);
        }

        IncidentActivity activity = new IncidentActivity(
                UUID.randomUUID(), incidentId, action, actor, normalizedSummary, OffsetDateTime.now());
        return repository.save(activity);
    }

    public List<IncidentActivity> findForIncident(UUID incidentId) {
        return repository.findByIncidentId(incidentId);
    }

    public IncidentActivityPage findPageForIncident(UUID incidentId, int page, int size) {
        return findPageForIncident(incidentId, null, page, size);
    }

    public IncidentActivityPage findPageForIncident(
            UUID incidentId, IncidentActivityAction action, int page, int size) {
        if (page < 0) {
            throw new InvalidIncidentPageRequestException("page must be zero or greater");
        }
        if (size < 1 || size > 100) {
            throw new InvalidIncidentPageRequestException("size must be between 1 and 100");
        }
        return repository.findPageByIncidentId(incidentId, action, page, size);
    }
}
