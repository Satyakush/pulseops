package com.satyakush.pulseops.auth;

import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class AuthSessionValidationTest {
    @Test void rejectsBlankToken() {
        assertThrows(IllegalArgumentException.class, () ->
            new AuthSession(UUID.randomUUID(), UUID.randomUUID(), "  ", OffsetDateTime.now().plusMinutes(5)));
    }

    @Test void treatsExpiryInstantAsExpired() {
        var session = new AuthSession(UUID.randomUUID(), UUID.randomUUID(), "token", OffsetDateTime.now());
        assertTrue(session.expired());
    }
}
