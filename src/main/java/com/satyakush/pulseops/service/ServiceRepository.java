package com.satyakush.pulseops.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServiceRepository {

    List<Service> findAll();

    Optional<Service> findById(UUID id);

    Service save(Service service);

    void deleteById(UUID id);
}
