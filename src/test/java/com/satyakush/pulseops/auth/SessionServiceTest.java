package com.satyakush.pulseops.auth;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class SessionServiceTest { @Test void revokedSessionCannotAuthenticate(){var users=new InMemoryUserRepository(); var sessions=new InMemoryAuthSessionRepository(); var user=users.save(new User(UUID.randomUUID(),"viewer","hash",AuthRole.VIEWER,UserStatus.ACTIVE)); var session=sessions.save(new AuthSession(UUID.randomUUID(),user.id(),"secret-token",java.time.OffsetDateTime.now().plusHours(1))); var service=new SessionService(sessions,users); assertEquals("viewer",service.authenticate(session.token()).username()); service.revoke(session.token()); assertThrows(SessionNotFoundException.class,()->service.authenticate(session.token()));} }
