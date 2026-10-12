package com.satyakush.pulseops.incident;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Profile("postgres")
public class JpaIncidentRepository implements IncidentRepository {

    private final JpaIncidentEntityRepository repository;

    public JpaIncidentRepository(JpaIncidentEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Incident> findAll() {
        return repository.findAll().stream()
                .map(IncidentEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<Incident> findById(UUID id) {
        return repository.findById(id).map(IncidentEntity::toDomain);
    }

    @Override
    public Incident save(Incident incident) {
        return repository.save(IncidentEntity.fromDomain(incident)).toDomain();
    }
}
