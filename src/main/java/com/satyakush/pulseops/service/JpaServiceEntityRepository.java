package com.satyakush.pulseops.service;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface JpaServiceEntityRepository extends JpaRepository<ServiceEntity, UUID> {
}
