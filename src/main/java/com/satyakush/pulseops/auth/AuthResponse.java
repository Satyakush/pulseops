package com.satyakush.pulseops.auth;

import java.time.OffsetDateTime;

public record AuthResponse(String token,OffsetDateTime expiresAt,String username,AuthRole role) { }
