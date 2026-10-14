package com.satyakush.pulseops.incident;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaIncidentActivityEntityRepository
        extends JpaRepository<IncidentActivityEntity, UUID> {
    List<IncidentActivityEntity> findByIncidentIdOrderByOccurredAtDescIdDesc(UUID incidentId);
    Page<IncidentActivityEntity> findByIncidentIdOrderByOccurredAtDescIdDesc(UUID incidentId, Pageable pageable);
    Page<IncidentActivityEntity> findByIncidentIdAndActionOrderByOccurredAtDescIdDesc(
            UUID incidentId, IncidentActivityAction action, Pageable pageable);
}
