package com.satyakush.pulseops.incident;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface JpaIncidentEntityRepository extends JpaRepository<IncidentEntity, UUID>, JpaSpecificationExecutor<IncidentEntity> {
}
