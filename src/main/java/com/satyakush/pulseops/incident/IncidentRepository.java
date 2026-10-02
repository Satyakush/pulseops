package com.satyakush.pulseops.incident;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IncidentRepository {
    List<Incident> findAll();
    Optional<Incident> findById(UUID id);
    Incident save(Incident incident);
}
