package com.satyakush.pulseops.auth;

import org.junit.jupiter.api.Test;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;

class LoginWrongPasswordTest {
    @Test void wrongPasswordDoesNotIssueSession() {
        var users = new InMemoryUserRepository();
        var passwords = new BCryptPasswordHasher();
        new RegistrationService(users, passwords).register(new RegisterRequest("operator1", "correct-pass"));
        var saves = new AtomicInteger();
        AuthSessionRepository sessions = new AuthSessionRepository() {
            public AuthSession save(AuthSession session) { saves.incrementAndGet(); return session; }
            public Optional<AuthSession> findByToken(String token) { return Optional.empty(); }
            public void deleteByToken(String token) { }
        };
        var login = new LoginService(users, passwords, new TokenService(), sessions);
        assertThrows(InvalidCredentialsException.class, () -> login.login(new LoginRequest("operator1", "wrong-pass")));
        assertEquals(0, saves.get());
    }
}
