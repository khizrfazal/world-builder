package com.my.worldbuilder.character.relationship;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CharacterRelationshipRepository extends JpaRepository<CharacterRelationship, UUID> {
    List<CharacterRelationship> findByWorldIdAndFromCharacterId(UUID worldId, UUID fromCharacterId);
}
