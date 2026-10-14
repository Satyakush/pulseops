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
    public IncidentActivityPage findPageByIncidentId(UUID incidentId, IncidentActivityAction action, int page, int size) {
        List<IncidentActivity> all = findByIncidentId(incidentId);
        if (action != null) {
            all = all.stream().filter(activity -> activity.action() == action).toList();
        }
        long offset = (long) page * size;
        int start = (int) Math.min(offset, all.size());
        int end = Math.min(start + size, all.size());
        int totalPages = all.isEmpty() ? 0 : (int) ((all.size() + (long) size - 1) / size);
        return new IncidentActivityPage(all.subList(start, end), page, size, all.size(), totalPages);
    }

    @Override
    public List<IncidentActivity> findByIncidentId(UUID incidentId) {
        return activities.values().stream()
                .filter(activity -> activity.incidentId().equals(incidentId))
                .sorted(NEWEST_FIRST)
                .toList();
    }
}
