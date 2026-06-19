package com.my.worldbuilder.relationship;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RelationshipRepository extends JpaRepository<Relationship, UUID> {
    List<Relationship> findByWorldId(UUID worldId);
}