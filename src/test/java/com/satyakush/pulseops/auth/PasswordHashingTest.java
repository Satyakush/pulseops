package com.satyakush.pulseops.auth;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PasswordHashingTest {
    @Test void bcryptHashMatchesOnlyOriginalPassword() {
        var hasher = new BCryptPasswordHasher();
        var hash = hasher.hash("correct-horse-battery");
        assertNotEquals("correct-horse-battery", hash);
        assertTrue(hasher.matches("correct-horse-battery", hash));
        assertFalse(hasher.matches("incorrect-password", hash));
    }
}
