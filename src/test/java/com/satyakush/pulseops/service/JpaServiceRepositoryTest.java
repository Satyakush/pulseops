package com.satyakush.pulseops.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaServiceRepository.class)
class JpaServiceRepositoryTest {

    @Autowired
    private ServiceRepository repository;

    @Test
    void savesAndLoadsService() {
        UUID id = UUID.randomUUID();
        Service service = new Service(
                id,
                "payments-api",
                "Payment processing API",
                ServiceStatus.OPERATIONAL
        );

        repository.save(service);

        assertThat(repository.findById(id))
                .contains(service);
    }

    @Test
    void updatesExistingService() {
        UUID id = UUID.randomUUID();
        repository.save(new Service(
                id,
                "alerts-api",
                "Alert delivery API",
                ServiceStatus.OPERATIONAL
        ));

        Service updated = new Service(
                id,
                "alerts-api",
                "Alert delivery API",
                ServiceStatus.DEGRADED
        );

        repository.save(updated);

        assertThat(repository.findById(id))
                .contains(updated);
    }

    @Test
    void deletesExistingService() {
        UUID id = UUID.randomUUID();
        repository.save(new Service(
                id,
                "orders-api",
                "Order processing API",
                ServiceStatus.OPERATIONAL
        ));

        repository.deleteById(id);

        assertThat(repository.findById(id)).isEmpty();
    }
}
