package com.satyakush.pulseops.service;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

import java.util.UUID;

@Entity
@Table(name = "services")
public class ServiceEntity {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private ServiceStatus status;

    protected ServiceEntity() {
    }

    public ServiceEntity(UUID id, String name, String description, ServiceStatus status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ServiceStatus getStatus() {
        return status;
    }

    public Service toDomain() {
        return new Service(id, name, description, status);
    }

    public static ServiceEntity fromDomain(Service service) {
        return new ServiceEntity(
                service.id(),
                service.name(),
                service.description(),
                service.status()
        );
    }
}
