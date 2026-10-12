package com.satyakush.pulseops.incident;

import java.util.List;

public record IncidentPage(
        List<Incident> items,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
}
