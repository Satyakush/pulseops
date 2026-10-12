package com.satyakush.pulseops.incident;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Persistence representation of an incident. Domain records stay independent
 * of JPA so the in-memory and database-backed repositories share one contract.
 */
@Entity
@Table(name = "incidents")
public class IncidentEntity {

    @Id
    private UUID id;

    @Column(name = "service_id", nullable = false)
    private UUID serviceId;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 4000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private IncidentSeverity severity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private IncidentStatus status;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    protected IncidentEntity() {
    }

    public IncidentEntity(
            UUID id,
            UUID serviceId,
            String title,
            String description,
            IncidentSeverity severity,
            IncidentStatus status,
            OffsetDateTime createdAt
    ) {
        this.id = id;
        this.serviceId = serviceId;
        this.title = title;
        this.description = description;
        this.severity = severity;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Incident toDomain() {
        return new Incident(id, serviceId, title, description, severity, status, createdAt);
    }

    public static IncidentEntity fromDomain(Incident incident) {
        return new IncidentEntity(
                incident.id(),
                incident.serviceId(),
                incident.title(),
                incident.description(),
                incident.severity(),
                incident.status(),
                incident.createdAt()
        );
    }
}
