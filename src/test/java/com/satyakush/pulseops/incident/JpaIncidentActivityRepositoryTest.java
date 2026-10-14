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
@Import(JpaIncidentActivityRepository.class)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:incident-activity-persistence-test;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class JpaIncidentActivityRepositoryTest {
    @Autowired
    private JpaIncidentActivityEntityRepository entityRepository;

    @Autowired
    private IncidentActivityRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void persistsActivityAndReturnsOnlyIncidentEntriesNewestFirst() {
        UUID incidentId = UUID.randomUUID();
        IncidentActivity older = activity(incidentId, "2026-10-14T10:00:00Z", "Incident created");
        IncidentActivity newer = activity(incidentId, "2026-10-14T11:00:00Z", "Incident status changed");
        IncidentActivity unrelated = activity(UUID.randomUUID(), "2026-10-14T12:00:00Z", "Unrelated incident");

        repository.save(older);
        repository.save(newer);
        repository.save(unrelated);
        entityRepository.flush();
        entityManager.clear();

        assertThat(repository.findByIncidentId(incidentId)).containsExactly(newer, older);
    }

    @Test
    void returnsEmptyListWhenIncidentHasNoActivity() {
        assertThat(repository.findByIncidentId(UUID.randomUUID())).isEmpty();
    }

    private IncidentActivity activity(UUID incidentId, String timestamp, String summary) {
        return new IncidentActivity(UUID.randomUUID(), incidentId, IncidentActivityAction.CREATED,
                "operator", summary, OffsetDateTime.parse(timestamp));
    }
}
