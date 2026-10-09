package com.satyakush.pulseops.auth;

import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class SessionServiceInactiveUserTest { @Test void disabledUserCannotUseExistingSession(){var users=new InMemoryUserRepository(); var sessions=new InMemoryAuthSessionRepository(); var id=UUID.randomUUID(); users.save(new User(id,"disabled","hash",AuthRole.VIEWER,UserStatus.DISABLED)); sessions.save(new AuthSession(UUID.randomUUID(),id,"token",OffsetDateTime.now().plusHours(1))); assertThrows(SessionNotFoundException.class,()->new SessionService(sessions,users).authenticate("token"));} }
