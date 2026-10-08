package com.satyakush.pulseops.auth;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class TokenServiceTest { @Test void issuedTokenExpiresInFuture(){var token=new TokenService().issue(new User(UUID.randomUUID(),"user","hash",AuthRole.VIEWER,UserStatus.ACTIVE)); assertNotNull(token.value()); assertTrue(token.expiresAt().isAfter(java.time.OffsetDateTime.now()));} }
