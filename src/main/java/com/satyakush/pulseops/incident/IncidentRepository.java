package com.satyakush.pulseops.incident;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Comparator;

public interface IncidentRepository {
    List<Incident> findAll();
    Optional<Incident> findById(UUID id);
    Incident save(Incident incident);

    default IncidentPage findPage(int page, int size, IncidentFilter filter, IncidentSeverity severity, UUID serviceId) {
        List<Incident> filtered = findAll().stream()
                .filter(incident -> filter == null || (filter == IncidentFilter.ACTIVE
                        ? incident.status() != IncidentStatus.RESOLVED : incident.status() == IncidentStatus.RESOLVED))
                .filter(incident -> severity == null || incident.severity() == severity)
                .filter(incident -> serviceId == null || incident.serviceId().equals(serviceId))
                .sorted(Comparator.comparing(Incident::createdAt).reversed())
                .toList();
        long offset = (long) page * size;
        int from = (int) Math.min(offset, filtered.size());
        int to = Math.min(from + size, filtered.size());
        int totalPages = (int) Math.ceil((double) filtered.size() / size);
        return new IncidentPage(filtered.subList(from, to), page, size, filtered.size(), totalPages);
    }
}
