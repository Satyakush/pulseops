package com.satyakush.pulseops.service;

import java.util.UUID;

public record Service(
        UUID id,
        String name,
        String description,
        ServiceStatus status
) {
}
