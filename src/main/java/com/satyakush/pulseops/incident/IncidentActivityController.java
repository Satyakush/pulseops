package com.satyakush.pulseops.incident;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Read-only API for the append-only audit history of an incident. */
@RestController
@RequestMapping("/api/v1/incidents/{incidentId}/activity")
public class IncidentActivityController {
    private final IncidentService incidentService;
    private final IncidentActivityService activityService;

    public IncidentActivityController(IncidentService incidentService, IncidentActivityService activityService) {
        this.incidentService = incidentService;
        this.activityService = activityService;
    }

    @GetMapping
    public List<IncidentActivity> findForIncident(@PathVariable UUID incidentId) {
        // Preserve the API's standard 404 response for unknown incidents.
        incidentService.findById(incidentId);
        return activityService.findForIncident(incidentId);
    }

    @GetMapping("/page")
    public IncidentActivityPage findPageForIncident(
            @PathVariable UUID incidentId,
            @org.springframework.web.bind.annotation.RequestParam(required = false) IncidentActivityAction action,
            @org.springframework.web.bind.annotation.RequestParam(defaultValue = "0") int page,
            @org.springframework.web.bind.annotation.RequestParam(defaultValue = "20") int size) {
        // Preserve the same 404 behavior as the unpaginated timeline.
        incidentService.findById(incidentId);
        return activityService.findPageForIncident(incidentId, action, page, size);
    }
}
