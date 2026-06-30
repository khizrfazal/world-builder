package com.my.worldbuilder.location.character;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CharacterLocationRepository extends JpaRepository<CharacterLocation, UUID> {
    List<CharacterLocation> findByWorldId(UUID worldId);
    List<CharacterLocation> findByWorldIdAndCharacterId(UUID worldId, UUID characterId);
}
