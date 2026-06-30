package com.my.worldbuilder.event.character;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EventCharacterRepository extends JpaRepository<EventCharacter, UUID> {
    List<EventCharacter> findByWorldId(UUID worldId);
    List<EventCharacter> findByWorldIdAndEventId(UUID worldId, UUID eventId);
}
