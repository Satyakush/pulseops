package com.satyakush.pulseops.incident;

import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("postgres")
public class JpaIncidentActivityRepository implements IncidentActivityRepository {
    private final JpaIncidentActivityEntityRepository repository;

    public JpaIncidentActivityRepository(JpaIncidentActivityEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public IncidentActivity save(IncidentActivity activity) {
        return repository.save(IncidentActivityEntity.fromDomain(activity)).toDomain();
    }

    @Override
    public List<IncidentActivity> findByIncidentId(UUID incidentId) {
        return repository.findByIncidentIdOrderByOccurredAtDescIdDesc(incidentId).stream()
                .map(IncidentActivityEntity::toDomain)
                .toList();
    }
}
