package com.satyakush.pulseops.realtime;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class PulseOpsEventPublisher {
    private final ApplicationEventPublisher publisher;

    public PulseOpsEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publish(PulseOpsEvent event) {
        publisher.publishEvent(event);
    }
}
