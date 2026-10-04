package com.satyakush.pulseops.realtime;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class PulseOpsEventControllerTest {
    @Test
    void subscribeReturnsEmitter() {
        PulseOpsEventStream stream = new PulseOpsEventStream();
        PulseOpsEventController controller = new PulseOpsEventController(stream);

        SseEmitter emitter = controller.subscribe();

        assertNotNull(emitter);
    }
}
