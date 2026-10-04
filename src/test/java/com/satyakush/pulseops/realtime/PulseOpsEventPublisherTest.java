package com.satyakush.pulseops.realtime;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class PulseOpsEventPublisherTest {
    @Test
    void publishesOperationsEvent() {
        ApplicationEventPublisher applicationPublisher = mock(ApplicationEventPublisher.class);
        PulseOpsEventPublisher publisher = new PulseOpsEventPublisher(applicationPublisher);
        PulseOpsEvent event = new PulseOpsEvent(UUID.randomUUID(), "SERVICE_CREATED", "service", UUID.randomUUID(), OffsetDateTime.now());

        publisher.publish(event);

        verify(applicationPublisher).publishEvent(event);
    }
}
