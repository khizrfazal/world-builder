package com.my.worldbuilder.event;

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

    @Transactional
    public UUID createEvent(UUID worldId, EventRequest request) {
        var eventEntity = eventMapper.toEntity(request);
        eventEntity.setWorldId(worldId);
        return eventRepository.save(eventEntity).getId();
    }

    public List<EventResponse> getEventsByWorld(UUID worldId) {
        return eventRepository.findByWorldId(worldId)
                .stream()
                .map(eventMapper::toResponse)
                .toList();
    }

    public EventResponse getEventById(UUID id) {
        return eventRepository.findById(id)
                .map(eventMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    @Transactional
    public void updateEvent(UUID id, EventRequest request) {
        var eventEntity = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        eventMapper.updateEntityFromRequest(request, eventEntity);
        eventRepository.save(eventEntity);
    }

    @Transactional
    public void deleteEvent(UUID id) {
        eventRepository.deleteById(id);
    }
}