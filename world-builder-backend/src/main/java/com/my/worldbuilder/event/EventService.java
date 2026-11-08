
package com.my.worldbuilder.event;

import com.my.worldbuilder.common.exception.EventNotFoundException;
import com.my.worldbuilder.common.security.WorldOwnershipService;
import com.my.worldbuilder.event.dto.EventRequest;
import com.my.worldbuilder.event.dto.EventResponse;
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
    private final WorldOwnershipService worldOwnershipService;

    @Transactional
    public UUID createEvent(UUID worldId, EventRequest request, UUID userId) {
        var world = worldOwnershipService.getOwnedWorld(worldId, userId);
        var eventEntity = eventMapper.toEntity(request);
        eventEntity.setWorld(world);
        return eventRepository.save(eventEntity).getId();
    }

    @Transactional(readOnly = true)
    public List<EventResponse> getEventsByWorld(UUID worldId, UUID userId) {
        worldOwnershipService.getOwnedWorld(worldId, userId);
        return eventRepository.findByWorld_Id(worldId)
                .stream()
                .map(eventMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public EventResponse getEventById(UUID id, UUID userId) {
        var eventEntity = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException(id));
        worldOwnershipService.assertOwnership(eventEntity.getWorld(), userId);
        return eventMapper.toResponse(eventEntity);
    }

    @Transactional
    public void updateEvent(UUID id, EventRequest request, UUID userId) {
        var eventEntity = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException(id));
        worldOwnershipService.assertOwnership(eventEntity.getWorld(), userId);
        eventMapper.updateEntityFromRequest(request, eventEntity);
    }

    @Transactional
    public void deleteEvent(UUID id, UUID userId) {
        var eventEntity = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException(id));
        worldOwnershipService.assertOwnership(eventEntity.getWorld(), userId);
        eventRepository.delete(eventEntity);
    }
}