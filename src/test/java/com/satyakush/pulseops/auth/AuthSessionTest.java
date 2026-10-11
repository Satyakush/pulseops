package com.satyakush.pulseops.auth;

import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class AuthSessionTest { @Test void futureSessionIsNotExpired(){var session=new AuthSession(UUID.randomUUID(),UUID.randomUUID(),"token",OffsetDateTime.now().plusHours(1)); assertFalse(session.expired());} 
    @Test void pastExpiryIsExpired() { var session = new AuthSession(UUID.randomUUID(), UUID.randomUUID(), "old-token", OffsetDateTime.now().minusMinutes(1)); assertTrue(session.expired()); }
}
