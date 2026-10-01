package com.satyakush.pulseops.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class ServiceService {

    private final ServiceRepository repository;

    public ServiceService(ServiceRepository repository) {
        this.repository = repository;
    }

    public List<Service> findAll() {
        return repository.findAll();
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
        return repository.save(service);
    }

    public void delete(UUID id) {
        findById(id);
        repository.deleteById(id);
    }

    public Service updateStatus(UUID id, ServiceStatus status) {
        Service service = findById(id);
        Service updated = new Service(service.id(), service.name(), service.description(), status);
        return repository.save(updated);
    }
}
