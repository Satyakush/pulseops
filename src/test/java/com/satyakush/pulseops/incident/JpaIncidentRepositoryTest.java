package com.satyakush.pulseops.incident;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("postgres")
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
        entityRepository.clear();

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
}
