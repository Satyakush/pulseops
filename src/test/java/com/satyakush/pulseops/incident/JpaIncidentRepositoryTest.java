package com.satyakush.pulseops.incident;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("postgres")
@Import(JpaIncidentRepository.class)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:incident-persistence-test;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class JpaIncidentRepositoryTest {

    @Autowired
    private JpaIncidentEntityRepository entityRepository;

    @Autowired
    private IncidentRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void persistsAndReloadsIncidentIncludingEnumsAndTimestamp() {
        UUID id = UUID.randomUUID();
        OffsetDateTime createdAt = OffsetDateTime.parse("2026-10-12T12:30:00Z");
        Incident incident = new Incident(
                id,
                UUID.randomUUID(),
                "Payment latency",
                "p95 latency is above the threshold",
                IncidentSeverity.CRITICAL,
                IncidentStatus.INVESTIGATING,
                createdAt
        );

        repository.save(incident);
        entityRepository.flush();
        entityManager.clear();

        assertThat(repository.findById(id)).contains(incident);
    }

    @Test
    void savingExistingIncidentUpdatesItsStatus() {
        UUID id = UUID.randomUUID();
        Incident initial = new Incident(
                id, UUID.randomUUID(), "Queue backlog", null,
                IncidentSeverity.HIGH, IncidentStatus.OPEN, OffsetDateTime.now()
        );
        repository.save(initial);

        Incident updated = new Incident(
                initial.id(), initial.serviceId(), initial.title(), initial.description(),
                initial.severity(), IncidentStatus.RESOLVED, initial.createdAt()
        );
        repository.save(updated);

        assertThat(repository.findById(id)).contains(updated);
    }

    @Test
    void findAllReturnsPersistedIncidents() {
        Incident incident = new Incident(
                UUID.randomUUID(), UUID.randomUUID(), "Database saturation", null,
                IncidentSeverity.MEDIUM, IncidentStatus.OPEN, OffsetDateTime.now()
        );

        repository.save(incident);

        assertThat(repository.findAll()).contains(incident);
    }

    @Test
    void databasePageAppliesFiltersAndOrdersNewestFirst() {
        UUID serviceId = UUID.randomUUID();
        Incident older = new Incident(UUID.randomUUID(), serviceId, "Older", null,
                IncidentSeverity.LOW, IncidentStatus.OPEN, OffsetDateTime.parse("2026-10-12T10:00:00Z"));
        Incident newer = new Incident(UUID.randomUUID(), serviceId, "Newer", null,
                IncidentSeverity.HIGH, IncidentStatus.OPEN, OffsetDateTime.parse("2026-10-12T11:00:00Z"));
        repository.save(older);
        repository.save(newer);

        IncidentPage result = repository.findPage(0, 1, IncidentFilter.ACTIVE, null, serviceId);

        assertThat(result.items()).containsExactly(newer);
        assertThat(result.totalElements()).isEqualTo(2);
        assertThat(result.totalPages()).isEqualTo(2);
    }
}
