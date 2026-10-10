package com.satyakush.pulseops.realtime;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.time.OffsetDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PulseOpsEventStreamTest {
    @Test
    void subscriptionRegistersEmitter() {
        PulseOpsEventStream stream = new PulseOpsEventStream();
        SseEmitter emitter = stream.subscribe();
        assertNotNull(emitter);
        assertEquals(1, stream.subscriberCount());
        stream.shutdown();
    }

    @Test
    void shutdownCompletesActiveStream() {
        PulseOpsEventStream stream = new PulseOpsEventStream();
        stream.subscribe();
        stream.shutdown();
        assertEquals(0, stream.subscriberCount());
    }

    @Test
    void failedEmitterIsRemovedDuringBroadcast() {
        PulseOpsEventStream stream = new PulseOpsEventStream();
        stream.subscribe();
        stream.broadcast(new PulseOpsEvent(
                UUID.randomUUID(), PulseOpsEventType.SERVICE_CREATED, "service", UUID.randomUUID(), OffsetDateTime.now()
        ));
        assertEquals(1, stream.subscriberCount());
        stream.shutdown();
    }
}
