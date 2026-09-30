package com.satyakush.pulseops.service;

import jakarta.validation.constraints.NotNull;

public record UpdateServiceStatusRequest(@NotNull ServiceStatus status) {
}
