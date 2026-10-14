package com.satyakush.pulseops.incident;

import java.util.List;

/** Stable response contract for bounded incident activity queries. */
public record IncidentActivityPage(
        List<IncidentActivity> items,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
    public IncidentActivityPage {
        items = List.copyOf(items);
    }
}
