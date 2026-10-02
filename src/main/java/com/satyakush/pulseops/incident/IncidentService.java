package com.satyakush.pulseops.incident;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class IncidentService {
    private final IncidentRepository repository;

    public IncidentService(IncidentRepository repository) {
        this.repository = repository;
    }

    public List<Incident> findAll() {
        return repository.findAll().stream()
                .sorted(Comparator.comparing(Incident::createdAt).reversed())
                .toList();
    }

    public Incident findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new IncidentNotFoundException(id));
    }

    public Incident updateStatus(UUID id, IncidentStatus status) {
        Incident incident = findById(id);
        return repository.save(new Incident(incident.id(), incident.serviceId(), incident.title(), incident.description(), incident.severity(), status, incident.createdAt()));
    }

    public Incident create(CreateIncidentRequest request) {
        Incident incident = new Incident(
                UUID.randomUUID(), request.serviceId(), request.title().trim(), request.description(),
                request.severity(), IncidentStatus.OPEN, OffsetDateTime.now()
        );
        return repository.save(incident);
    }
}
