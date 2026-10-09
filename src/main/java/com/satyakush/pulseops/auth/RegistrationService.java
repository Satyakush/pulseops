package com.satyakush.pulseops.auth;

import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
    private final UserRepository users;
    private final PasswordHasher passwords;

    public RegistrationService(UserRepository users, PasswordHasher passwords) {
        this.users = users;
        this.passwords = passwords;
    }

    public User register(RegisterRequest request) {
        String username = request.username().trim();
        if (users.existsByUsernameIgnoreCase(username)) {
            throw new DuplicateUsernameException(username);
        }
        User user = new User(UUID.randomUUID(), username, passwords.hash(request.password()), AuthRole.VIEWER, UserStatus.ACTIVE);
        return users.save(user);
    }
}
