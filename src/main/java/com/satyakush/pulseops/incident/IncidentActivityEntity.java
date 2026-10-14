package com.satyakush.pulseops.incident;

import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/** JPA representation kept separate from the API/domain record. */
@Entity
@Table(name = "incident_activity", indexes = {
        @Index(name = "idx_incident_activity_incident_time", columnList = "incident_id, occurred_at")
})
public class IncidentActivityEntity {
    @Id
    private UUID id;

    @Column(name = "incident_id", nullable = false)
    private UUID incidentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private IncidentActivityAction action;

    @Column(nullable = false, length = 120)
    private String actor;

    @Column(nullable = false, length = 500)
    private String summary;

    @Column(name = "occurred_at", nullable = false)
    private OffsetDateTime occurredAt;

    protected IncidentActivityEntity() {
    }

    public IncidentActivityEntity(UUID id, UUID incidentId, IncidentActivityAction action,
                                  String actor, String summary, OffsetDateTime occurredAt) {
        this.id = id;
        this.incidentId = incidentId;
        this.action = action;
        this.actor = actor;
        this.summary = summary;
        this.occurredAt = occurredAt;
    }

    public IncidentActivity toDomain() {
        return new IncidentActivity(id, incidentId, action, actor, summary, occurredAt);
    }

    public static IncidentActivityEntity fromDomain(IncidentActivity activity) {
        return new IncidentActivityEntity(activity.id(), activity.incidentId(), activity.action(),
                activity.actor(), activity.summary(), activity.occurredAt());
    }
}
