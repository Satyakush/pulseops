package com.satyakush.pulseops.incident;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/incidents")
public class IncidentController {
    private final IncidentService incidentService;

    public IncidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @GetMapping
    public List<Incident> findAll() {
        return incidentService.findAll();
    }

    @PatchMapping("/{id}/status")
    public Incident updateStatus(@PathVariable UUID id, @Valid @RequestBody UpdateIncidentStatusRequest request) {
        return incidentService.updateStatus(id, request.status());
    }

    @GetMapping("/summary")
    public IncidentStatusSummary getSummary() {
        return incidentService.getSummary();
    }

    @GetMapping("/{id}")
    public Incident findById(@PathVariable UUID id) {
        return incidentService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Incident create(@Valid @RequestBody CreateIncidentRequest request) {
        return incidentService.create(request);
    }
}
