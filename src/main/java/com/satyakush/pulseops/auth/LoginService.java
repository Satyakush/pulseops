package com.satyakush.pulseops.auth;

import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private final UserRepository users;
    private final PasswordHasher passwords;
    private final TokenService tokens;
    private final AuthSessionRepository sessions;

    public LoginService(UserRepository users, PasswordHasher passwords, TokenService tokens, AuthSessionRepository sessions) {
        this.users = users;
        this.passwords = passwords;
        this.tokens = tokens;
        this.sessions = sessions;
    }

    public AuthResponse login(LoginRequest request) {
        User user = users.findByUsernameIgnoreCase(request.username().trim()).orElseThrow(InvalidCredentialsException::new);
        if (!user.active() || !passwords.matches(request.password(), user.passwordHash())) throw new InvalidCredentialsException();
        AuthToken token = tokens.issue(user);
        sessions.save(new AuthSession(UUID.randomUUID(), user.id(), token.value(), token.expiresAt()));
        return new AuthResponse(token.value(), token.expiresAt(), user.username(), user.role());
    }
}
