package com.satyakush.pulseops.realtime;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PulseOpsEventListener {
    private final PulseOpsEventStream stream;

    public PulseOpsEventListener(PulseOpsEventStream stream) {
        this.stream = stream;
    }

    @EventListener
    public void onEvent(PulseOpsEvent event) {
        stream.broadcast(event);
    }
}
