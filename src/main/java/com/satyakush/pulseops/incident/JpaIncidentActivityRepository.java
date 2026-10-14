package com.satyakush.pulseops.incident;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;

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

    @Override
    public IncidentActivityPage findPageByIncidentId(UUID incidentId, int page, int size) {
        var result = repository.findByIncidentIdOrderByOccurredAtDescIdDesc(incidentId,
                PageRequest.of(page, size));
        return new IncidentActivityPage(
                result.getContent().stream().map(IncidentActivityEntity::toDomain).toList(),
                result.getNumber(), result.getSize(), result.getTotalElements(), result.getTotalPages());
    }
}
