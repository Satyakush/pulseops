package com.satyakush.pulseops.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Profile("postgres")
public class JpaServiceRepository implements ServiceRepository {

    private final JpaServiceEntityRepository repository;

    public JpaServiceRepository(JpaServiceEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Service> findAll() {
        return repository.findAll().stream()
                .map(ServiceEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<Service> findById(UUID id) {
        return repository.findById(id)
                .map(ServiceEntity::toDomain);
    }

    @Override
    public boolean existsByNameIgnoreCase(String name) {
        return repository.existsByNameIgnoreCase(name);
    }

    @Override
    public Service save(Service service) {
        return repository.save(ServiceEntity.fromDomain(service)).toDomain();
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}
