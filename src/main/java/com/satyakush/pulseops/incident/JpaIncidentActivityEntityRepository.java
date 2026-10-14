package com.satyakush.pulseops.incident;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaIncidentActivityEntityRepository
        extends JpaRepository<IncidentActivityEntity, UUID> {
    List<IncidentActivityEntity> findByIncidentIdOrderByOccurredAtDescIdDesc(UUID incidentId);
}
