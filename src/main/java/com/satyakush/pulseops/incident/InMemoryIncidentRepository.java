package com.satyakush.pulseops.incident;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("!postgres")
public class InMemoryIncidentRepository implements IncidentRepository {
    private final ConcurrentMap<UUID, Incident> incidents = new ConcurrentHashMap<>();

    @Override
    public List<Incident> findAll() {
        return new ArrayList<>(incidents.values());
    }

    @Override
    public Optional<Incident> findById(UUID id) {
        return Optional.ofNullable(incidents.get(id));
    }

    @Override
    public Incident save(Incident incident) {
        incidents.put(incident.id(), incident);
        return incident;
    }
}
