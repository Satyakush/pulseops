package com.satyakush.pulseops.auth;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.Objects;

public record AuthSession(UUID id, UUID userId, String token, OffsetDateTime expiresAt) {
    public AuthSession {
        Objects.requireNonNull(id, "id is required");
        Objects.requireNonNull(userId, "userId is required");
        if (token == null || token.isBlank()) throw new IllegalArgumentException("token is required");
        Objects.requireNonNull(expiresAt, "expiresAt is required");
    }

    public boolean expired() {
        return !expiresAt.isAfter(OffsetDateTime.now());
    }
}
