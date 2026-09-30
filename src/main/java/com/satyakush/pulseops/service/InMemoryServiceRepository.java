package com.satyakush.pulseops.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Repository;

@Repository
public class InMemoryServiceRepository implements ServiceRepository {

    private final ConcurrentMap<UUID, Service> services = new ConcurrentHashMap<>();

    @Override
    public List<Service> findAll() {
        return new ArrayList<>(services.values());
    }

    @Override
    public Optional<Service> findById(UUID id) {
        return Optional.ofNullable(services.get(id));
    }

    @Override
    public Service save(Service service) {
        services.put(service.id(), service);
        return service;
    }

    @Override
    public void deleteById(UUID id) {
        services.remove(id);
    }
}
