package com.satyakush.pulseops.auth;

import java.util.Optional;

public interface AuthSessionRepository {
    AuthSession save(AuthSession session);
    Optional<AuthSession> findByToken(String token);
    void deleteByToken(String token);
}
