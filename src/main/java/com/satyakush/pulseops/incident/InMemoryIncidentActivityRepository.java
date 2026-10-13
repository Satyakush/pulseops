package com.satyakush.pulseops.incident;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("!postgres")
public class InMemoryIncidentActivityRepository implements IncidentActivityRepository {
    private static final Comparator<IncidentActivity> NEWEST_FIRST =
            Comparator.comparing(IncidentActivity::occurredAt).reversed()
                    .thenComparing(activity -> activity.id().toString(), Comparator.reverseOrder());

    private final ConcurrentMap<UUID, IncidentActivity> activities = new ConcurrentHashMap<>();

    @Override
    public IncidentActivity save(IncidentActivity activity) {
        activities.put(activity.id(), activity);
        return activity;
    }

    @Override
    public List<IncidentActivity> findByIncidentId(UUID incidentId) {
        return activities.values().stream()
                .filter(activity -> activity.incidentId().equals(incidentId))
                .sorted(NEWEST_FIRST)
                .toList();
    }
}
