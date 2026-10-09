package com.satyakush.pulseops.auth;

import java.util.UUID;

public record UserResponse(UUID id, String username, AuthRole role, UserStatus status) { public static UserResponse from(User user){ return new UserResponse(user.id(), user.username(), user.role(), user.status()); } }
