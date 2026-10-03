package com.satyakush.pulseops.security;

public record PulseOpsUser(
        String username,
        String password,
        PulseOpsRole role
) {
}
