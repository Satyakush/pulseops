package com.satyakush.pulseops.service;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/services")
public class ServiceController {

    private final ServiceService serviceService;

    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping
    public List<Service> findAll(@RequestParam(required = false) ServiceStatus status) {
        return status == null ? serviceService.findAll() : serviceService.findAll(status);
    }

    @GetMapping("/summary")
    public ServiceSummary getSummary() {
        return serviceService.getSummary();
    }

    @GetMapping("/{id}")
    public Service findById(@PathVariable UUID id) {
        return serviceService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Service create(@Valid @RequestBody CreateServiceRequest request) {
        return serviceService.create(request.name(), request.description());
    }

    @PatchMapping("/{id}/status")
    public Service updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateServiceStatusRequest request) {
        return serviceService.updateStatus(id, request.status());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        serviceService.delete(id);
    }
}
