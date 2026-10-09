package com.satyakush.pulseops.auth;

import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class SessionServiceExpiredTest { @Test void expiredSessionIsRejected(){var users=new InMemoryUserRepository(); var sessions=new InMemoryAuthSessionRepository(); var user=users.save(new User(UUID.randomUUID(),"viewer","hash",AuthRole.VIEWER,UserStatus.ACTIVE)); sessions.save(new AuthSession(UUID.randomUUID(),user.id(),"expired",OffsetDateTime.now().minusMinutes(1))); assertThrows(SessionNotFoundException.class,()->new SessionService(sessions,users).authenticate("expired"));} }
