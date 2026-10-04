package com.satyakush.pulseops.realtime;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v1/events")
public class PulseOpsEventController {
    private final PulseOpsEventStream stream;

    public PulseOpsEventController(PulseOpsEventStream stream) {
        this.stream = stream;
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe() {
        return stream.subscribe();
    }
}
