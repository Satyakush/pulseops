package com.satyakush.pulseops.service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.time.OffsetDateTime;

import com.satyakush.pulseops.realtime.PulseOpsEvent;
import com.satyakush.pulseops.realtime.PulseOpsEventPublisher;
import com.satyakush.pulseops.realtime.PulseOpsEventType;

import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import com.satyakush.pulseops.cache.PulseOpsCacheNames;

@Service
public class ServiceService {

    private final ServiceRepository repository;
    private final PulseOpsEventPublisher eventPublisher;

    public ServiceService(ServiceRepository repository, PulseOpsEventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    @Cacheable(PulseOpsCacheNames.SERVICES)
    public List<Service> findAll() {
        return repository.findAll().stream()
                .sorted(Comparator.comparing(Service::name, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    @Cacheable(value = PulseOpsCacheNames.SERVICES, key = "'status:' + #status")
    public List<Service> findAll(ServiceStatus status) {
        return findAll().stream()
                .filter(service -> service.status() == status)
                .toList();
    }

    @Cacheable(value = PulseOpsCacheNames.SERVICES, key = "'summary'")
    public ServiceSummary getSummary() {
        List<Service> services = repository.findAll();

        return new ServiceSummary(
                services.size(),
                services.stream().filter(service -> service.status() == ServiceStatus.OPERATIONAL).count(),
                services.stream().filter(service -> service.status() == ServiceStatus.DEGRADED).count(),
                services.stream().filter(service -> service.status() == ServiceStatus.OUTAGE).count()
        );
    }

    @Cacheable(value = PulseOpsCacheNames.SERVICES, key = "'id:' + #id")
    public Service findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException(id));
    }

    @CacheEvict(value = PulseOpsCacheNames.SERVICES, allEntries = true)
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
        publish(PulseOpsEventType.SERVICE_CREATED, saved.id());
        return saved;
    }

    @CacheEvict(value = PulseOpsCacheNames.SERVICES, allEntries = true)
    public void delete(UUID id) {
        findById(id);
        repository.deleteById(id);
        publish(PulseOpsEventType.SERVICE_DELETED, id);
    }

    @CacheEvict(value = PulseOpsCacheNames.SERVICES, allEntries = true)
    public Service updateStatus(UUID id, ServiceStatus status) {
        Service service = findById(id);
        Service updated = new Service(service.id(), service.name(), service.description(), status);
        Service saved = repository.save(updated);
        publish(PulseOpsEventType.SERVICE_STATUS_CHANGED, saved.id());
        return saved;
    }

    private void publish(PulseOpsEventType type, UUID resourceId) {
        eventPublisher.publish(new PulseOpsEvent(
                UUID.randomUUID(), type, "service", resourceId, OffsetDateTime.now()
        ));
    }
}
