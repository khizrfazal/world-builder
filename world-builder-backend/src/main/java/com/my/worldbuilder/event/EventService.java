package com.my.worldbuilder.event;

import com.my.worldbuilder.common.exception.CharacterNotFoundException;
import com.my.worldbuilder.common.exception.EventNotFoundException;
import com.my.worldbuilder.event.dto.EventRequest;
import com.my.worldbuilder.event.dto.EventResponse;
import com.my.worldbuilder.common.exception.ForbiddenException;
import com.my.worldbuilder.world.WorldRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final WorldRepository worldRepository;

    private void assertOwnership(Event event, UUID userId) {
        if (!event.getWorld().getUser().getId().equals(userId)) {
            throw new ForbiddenException("Not your event");
        }
    }

    @Transactional
    public UUID createEvent(UUID worldId, EventRequest request, UUID userId) {
        var world = worldRepository.findById(worldId)
                .orElseThrow(() -> new RuntimeException("World does not exist"));
        if (!world.getUser().getId().equals(userId)) {
            throw new ForbiddenException("Not your world");
        }
        var eventEntity = eventMapper.toEntity(request);
        eventEntity.setWorld(world);
        return eventRepository.save(eventEntity).getId();
    }

    public List<EventResponse> getEventsByWorld(UUID worldId, UUID userId) {
        var world = worldRepository.findById(worldId)
                .orElseThrow(() -> new RuntimeException("World does not exist"));
        if (!world.getUser().getId().equals(userId)) {
            throw new ForbiddenException("Not your world");
        }
        return eventRepository.findByWorld_Id(worldId)
                .stream()
                .map(eventMapper::toResponse)
                .toList();
    }

    public EventResponse getEventById(UUID id, UUID userId) {
        var eventEntity = eventRepository.findById(id)
                .orElseThrow(() -> new CharacterNotFoundException(id));
        assertOwnership(eventEntity, userId);
        return eventMapper.toResponse(eventEntity);
    }

    @Transactional
    public void updateEvent(UUID id, EventRequest request, UUID userId) {
        var eventEntity = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException(id));
        assertOwnership(eventEntity, userId);
        eventMapper.updateEntityFromRequest(request, eventEntity);
    }

    @Transactional
    public void deleteEvent(UUID id, UUID userId) {
        var eventEntity = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException(id));
        assertOwnership(eventEntity, userId);
        eventRepository.delete(eventEntity);
    }
}