package com.satyakush.pulseops.auth;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryUserRepositoryTest { @Test void usernameLookupIsCaseInsensitive(){var repo=new InMemoryUserRepository(); var user=new User(UUID.randomUUID(),"Satyam","hash",AuthRole.VIEWER,UserStatus.ACTIVE); repo.save(user); assertEquals(user,repo.findByUsernameIgnoreCase("SATYAM").orElseThrow());} }
