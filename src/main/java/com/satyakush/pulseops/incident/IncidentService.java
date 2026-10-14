package com.satyakush.pulseops.incident;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import com.satyakush.pulseops.realtime.PulseOpsEvent;
import com.satyakush.pulseops.realtime.PulseOpsEventPublisher;
import com.satyakush.pulseops.realtime.PulseOpsEventType;

import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import com.satyakush.pulseops.cache.PulseOpsCacheNames;

@Service
public class IncidentService {
    private final IncidentRepository repository;
    private final PulseOpsEventPublisher eventPublisher;
    private final IncidentActivityService activityService;

    public IncidentService(IncidentRepository repository, PulseOpsEventPublisher eventPublisher,
                           IncidentActivityService activityService) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
        this.activityService = activityService;
    }

    @Cacheable(PulseOpsCacheNames.INCIDENTS)
    public List<Incident> findAll() {
        return findAll(null);
    }

    @Cacheable(value = PulseOpsCacheNames.INCIDENTS, key = "'filter:' + #filter")
    public List<Incident> findAll(IncidentFilter filter) {
        return filterIncidents(repository.findAll(), filter, null, null);
    }

    @Cacheable(value = PulseOpsCacheNames.INCIDENTS, key = "'query:' + #filter + ':' + #severity + ':' + #serviceId")
    public List<Incident> findAll(IncidentFilter filter, IncidentSeverity severity, UUID serviceId) {
        return filterIncidents(repository.findAll(), filter, severity, serviceId);
    }

    private List<Incident> filterIncidents(
            List<Incident> incidents, IncidentFilter filter, IncidentSeverity severity, UUID serviceId) {
        return incidents.stream()
                .filter(incident -> filter == null || (filter == IncidentFilter.ACTIVE
                        ? incident.status() != IncidentStatus.RESOLVED
                        : incident.status() == IncidentStatus.RESOLVED))
                .filter(incident -> severity == null || incident.severity() == severity)
                .filter(incident -> serviceId == null || incident.serviceId().equals(serviceId))
                .sorted(Comparator.comparing(Incident::createdAt).reversed())
                .toList();
    }

    @Cacheable(value = PulseOpsCacheNames.INCIDENTS,
            key = "'page:' + #page + ':' + #size + ':' + #filter + ':' + #severity + ':' + #serviceId")
    public IncidentPage findPage(int page, int size, IncidentFilter filter,
                                 IncidentSeverity severity, UUID serviceId) {
        if (page < 0) {
            throw new InvalidIncidentPageRequestException("page must be zero or greater");
        }
        if (size < 1 || size > 100) {
            throw new InvalidIncidentPageRequestException("size must be between 1 and 100");
        }
        return repository.findPage(page, size, filter, severity, serviceId);
    }

    @Cacheable(value = PulseOpsCacheNames.INCIDENTS, key = "'id:' + #id")
    public Incident findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new IncidentNotFoundException(id));
    }

    @CacheEvict(value = PulseOpsCacheNames.INCIDENTS, allEntries = true)
    public Incident updateDetails(UUID id, UpdateIncidentRequest request) {
        Incident incident = findById(id);
        Incident updated = repository.save(new Incident(
                incident.id(), incident.serviceId(), request.title().trim(), request.description(),
                request.severity(), incident.status(), incident.createdAt()
        ));
        activityService.record(updated.id(), IncidentActivityAction.DETAILS_UPDATED,
                "Incident details updated");
        publish(PulseOpsEventType.INCIDENT_UPDATED, updated.id());
        return updated;
    }

    @CacheEvict(value = PulseOpsCacheNames.INCIDENTS, allEntries = true)
    public Incident updateStatus(UUID id, IncidentStatus status) {
        Incident incident = findById(id);
        Incident updated = repository.save(new Incident(incident.id(), incident.serviceId(), incident.title(), incident.description(), incident.severity(), status, incident.createdAt()));
        activityService.record(updated.id(), IncidentActivityAction.STATUS_CHANGED,
                "Incident status changed to " + status.name());
        publish(PulseOpsEventType.INCIDENT_STATUS_CHANGED, updated.id());
        return updated;
    }

    @Cacheable(value = PulseOpsCacheNames.INCIDENTS, key = "'summary'")
    public IncidentStatusSummary getSummary() {
        List<Incident> incidents = repository.findAll();
        return new IncidentStatusSummary(incidents.size(), count(incidents, IncidentStatus.OPEN), count(incidents, IncidentStatus.INVESTIGATING), count(incidents, IncidentStatus.RESOLVED));
    }

    private long count(List<Incident> incidents, IncidentStatus status) {
        return incidents.stream().filter(incident -> incident.status() == status).count();
    }

    @CacheEvict(value = PulseOpsCacheNames.INCIDENTS, allEntries = true)
    public Incident create(CreateIncidentRequest request) {
        Incident incident = new Incident(
                UUID.randomUUID(), request.serviceId(), request.title().trim(), request.description(),
                request.severity(), IncidentStatus.OPEN, OffsetDateTime.now()
        );
        Incident saved = repository.save(incident);
        activityService.record(saved.id(), IncidentActivityAction.CREATED, "Incident created");
        publish(PulseOpsEventType.INCIDENT_CREATED, saved.id());
        return saved;
    }

    private void publish(PulseOpsEventType type, UUID resourceId) {
        eventPublisher.publish(new PulseOpsEvent(
                UUID.randomUUID(), type, "incident", resourceId, OffsetDateTime.now()
        ));
    }
}
