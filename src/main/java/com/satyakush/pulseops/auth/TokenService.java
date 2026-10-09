package com.satyakush.pulseops.auth;

import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    public AuthToken issue(User user) {
        if (user == null || !user.active()) throw new IllegalArgumentException("An active user is required to issue a token");
        return new AuthToken(UUID.randomUUID().toString(), OffsetDateTime.now().plusHours(8));
    }
}
