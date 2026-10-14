package com.satyakush.pulseops.incident;

import java.util.List;
import java.util.UUID;

/** Persistence port for the append-only incident activity timeline. */
public interface IncidentActivityRepository {
    IncidentActivity save(IncidentActivity activity);
    List<IncidentActivity> findByIncidentId(UUID incidentId);
    default IncidentActivityPage findPageByIncidentId(UUID incidentId, int page, int size) {
        return findPageByIncidentId(incidentId, null, page, size);
    }
    IncidentActivityPage findPageByIncidentId(UUID incidentId, IncidentActivityAction action, int page, int size);
}
