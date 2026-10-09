package com.satyakush.pulseops.auth;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RegistrationUsernameNormalizationTest {
    @Test void trimsUsernameBeforeSaving() {
        var users = new InMemoryUserRepository();
        var registered = new RegistrationService(users, new BCryptPasswordHasher()).register(new RegisterRequest("  satyam  ", "long-password"));
        assertEquals("satyam", registered.username());
        assertTrue(users.findByUsernameIgnoreCase("SATYAM").isPresent());
    }
}
