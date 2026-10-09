package com.satyakush.pulseops.auth;

import java.util.Objects;
import java.util.UUID;

public record User(UUID id, String username, String passwordHash, AuthRole role, UserStatus status) {
    public User {
        Objects.requireNonNull(id, "id is required");
        if (username == null || username.isBlank()) throw new IllegalArgumentException("username is required");
        if (passwordHash == null || passwordHash.isBlank()) throw new IllegalArgumentException("password hash is required");
        Objects.requireNonNull(role, "role is required");
        Objects.requireNonNull(status, "status is required");
    }

    public boolean active() {
        return status == UserStatus.ACTIVE;
    }
}
