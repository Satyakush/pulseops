package com.satyakush.pulseops.auth;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository { Optional<User> findById(UUID id); Optional<User> findByUsernameIgnoreCase(String username); User save(User user); boolean existsByUsernameIgnoreCase(String username); }
