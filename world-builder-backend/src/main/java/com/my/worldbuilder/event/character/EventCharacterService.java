package com.my.worldbuilder.event.character;

import com.my.worldbuilder.event.character.dto.EventCharacterRequest;
import com.my.worldbuilder.event.character.dto.EventCharacterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventCharacterService {

    private final EventCharacterRepository eventCharacterRepository;
    private final EventCharacterMapper eventCharacterMapper;

    @Transactional
    public UUID assignCharacterToEvent(UUID worldId, EventCharacterRequest request) {
        var eventCharacterEntity = eventCharacterMapper.toEntity(request);
        eventCharacterEntity.setWorldId(worldId);
        return eventCharacterRepository.save(eventCharacterEntity).getId();
    }

    public List<EventCharacterResponse> getCharactersForEvent(UUID worldId, UUID eventId) {
        return eventCharacterRepository.findByWorldIdAndEventId(worldId, eventId)
                .stream()
                .map(eventCharacterMapper::toResponse)
                .toList();
    }

    public EventCharacterResponse getEventCharacterById(UUID id) {
        return eventCharacterRepository.findById(id)
                .map(eventCharacterMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("EventCharacter not found"));
    }

    @Transactional
    public void updateEventCharacter(UUID id, EventCharacterRequest request) {
        var eventCharacterEntity = eventCharacterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("EventCharacter not found"));
        eventCharacterMapper.updateEntityFromRequest(request, eventCharacterEntity);
        eventCharacterRepository.save(eventCharacterEntity);
    }

    @Transactional
    public void deleteEventCharacter(UUID id) {
        eventCharacterRepository.deleteById(id);
    }
}