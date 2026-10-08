package com.satyakush.pulseops.auth;

import java.time.OffsetDateTime;

public record AuthToken(String value, OffsetDateTime expiresAt) { }
