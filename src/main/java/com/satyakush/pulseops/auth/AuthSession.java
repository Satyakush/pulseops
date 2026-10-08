package com.satyakush.pulseops.auth;

import java.time.OffsetDateTime;
import java.util.UUID;

public record AuthSession(UUID id, UUID userId, String token, OffsetDateTime expiresAt) { public boolean expired(){return expiresAt.isBefore(OffsetDateTime.now());} }
