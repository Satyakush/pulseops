package com.satyakush.pulseops.incident;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

@Repository
@Profile("postgres")
public class JpaIncidentRepository implements IncidentRepository {

    private final JpaIncidentEntityRepository repository;

    public JpaIncidentRepository(JpaIncidentEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Incident> findAll() {
        return repository.findAll().stream()
                .map(IncidentEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<Incident> findById(UUID id) {
        return repository.findById(id).map(IncidentEntity::toDomain);
    }

    @Override
    public Incident save(Incident incident) {
        return repository.save(IncidentEntity.fromDomain(incident)).toDomain();
    }

    @Override
    public IncidentPage findPage(int page, int size, IncidentFilter filter,
                                 IncidentSeverity severity, UUID serviceId) {
        Specification<IncidentEntity> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filter == IncidentFilter.ACTIVE) {
                predicates.add(criteriaBuilder.notEqual(root.get("status"), IncidentStatus.RESOLVED));
            } else if (filter == IncidentFilter.RESOLVED) {
                predicates.add(criteriaBuilder.equal(root.get("status"), IncidentStatus.RESOLVED));
            }
            if (severity != null) {
                predicates.add(criteriaBuilder.equal(root.get("severity"), severity));
            }
            if (serviceId != null) {
                predicates.add(criteriaBuilder.equal(root.get("serviceId"), serviceId));
            }
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
        Page<IncidentEntity> result = repository.findAll(specification,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
        return new IncidentPage(result.getContent().stream().map(IncidentEntity::toDomain).toList(),
                result.getNumber(), result.getSize(), result.getTotalElements(), result.getTotalPages());
    }
}
