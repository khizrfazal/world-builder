package com.my.worldbuilder.faction.location;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FactionLocationRepository extends JpaRepository<FactionLocation, UUID> {
    List<FactionLocation> findByWorldId(UUID worldId);
    List<FactionLocation> findByWorldIdAndFactionId(UUID worldId, UUID factionId);
}
