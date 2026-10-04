package com.satyakush.pulseops.realtime;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class PulseOpsEventListenerTest {
    @Test
    void forwardsApplicationEventToStream() {
        PulseOpsEventStream stream = mock(PulseOpsEventStream.class);
        PulseOpsEventListener listener = new PulseOpsEventListener(stream);
        PulseOpsEvent event = new PulseOpsEvent(UUID.randomUUID(), "INCIDENT_CREATED", "incident", UUID.randomUUID(), OffsetDateTime.now());

        listener.onEvent(event);

        verify(stream).broadcast(event);
    }
}
