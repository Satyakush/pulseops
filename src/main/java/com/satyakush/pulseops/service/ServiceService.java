package com.satyakush.pulseops.service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.time.OffsetDateTime;

import com.satyakush.pulseops.realtime.PulseOpsEvent;
import com.satyakush.pulseops.realtime.PulseOpsEventPublisher;

import org.springframework.stereotype.Service;

@Service
public class ServiceService {

    private final ServiceRepository repository;
    private final PulseOpsEventPublisher eventPublisher;

    public ServiceService(ServiceRepository repository, PulseOpsEventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    public List<Service> findAll() {
        return repository.findAll().stream()
                .sorted(Comparator.comparing(Service::name, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    public List<Service> findAll(ServiceStatus status) {
        return findAll().stream()
                .filter(service -> service.status() == status)
                .toList();
    }

    public ServiceSummary getSummary() {
        List<Service> services = repository.findAll();

        return new ServiceSummary(
                services.size(),
                services.stream().filter(service -> service.status() == ServiceStatus.OPERATIONAL).count(),
                services.stream().filter(service -> service.status() == ServiceStatus.DEGRADED).count(),
                services.stream().filter(service -> service.status() == ServiceStatus.OUTAGE).count()
        );
    }

    public Service findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException(id));
    }

    public Service create(String name, String description) {
        String normalizedName = name.trim();
        if (repository.existsByNameIgnoreCase(normalizedName)) {
            throw new DuplicateServiceNameException(normalizedName);
        }

        Service service = new Service(
                UUID.randomUUID(),
                normalizedName,
                description,
                ServiceStatus.OPERATIONAL
        );
        Service saved = repository.save(service);
        publish("SERVICE_CREATED", saved.id());
        return saved;
    }

    public void delete(UUID id) {
        findById(id);
        repository.deleteById(id);
        publish("SERVICE_DELETED", id);
    }

    public Service updateStatus(UUID id, ServiceStatus status) {
        Service service = findById(id);
        Service updated = new Service(service.id(), service.name(), service.description(), status);
        Service saved = repository.save(updated);
        publish("SERVICE_STATUS_CHANGED", saved.id());
        return saved;
    }

    private void publish(String type, UUID resourceId) {
        eventPublisher.publish(new PulseOpsEvent(
                UUID.randomUUID(), type, "service", resourceId, OffsetDateTime.now()
        ));
    }
}
