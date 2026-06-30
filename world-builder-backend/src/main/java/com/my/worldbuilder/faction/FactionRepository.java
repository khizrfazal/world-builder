package com.my.worldbuilder.faction;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FactionRepository extends JpaRepository<Faction, UUID> {
    List<Faction> findByWorldId(UUID worldId);
}