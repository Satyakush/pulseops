package com.satyakush.pulseops.auth;

import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class SessionRepositoryTest { @Test void revocationRemovesTokenLookup(){var repo=new InMemoryAuthSessionRepository(); var session=new AuthSession(UUID.randomUUID(),UUID.randomUUID(),"token",OffsetDateTime.now().plusHours(1)); repo.save(session); assertTrue(repo.findByToken("token").isPresent()); repo.deleteByToken("token"); assertTrue(repo.findByToken("token").isEmpty());} }
