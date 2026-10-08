package com.satyakush.pulseops.auth;

import java.util.UUID;

public record User(UUID id, String username, String passwordHash, AuthRole role, UserStatus status) { public boolean active(){ return status == UserStatus.ACTIVE; } }
