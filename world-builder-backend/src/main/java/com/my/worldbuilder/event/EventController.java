package com.my.worldbuilder.event;

import com.my.worldbuilder.event.dto.EventRequest;
import com.my.worldbuilder.event.dto.EventResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class EventController {

    private final EventService service;

    @PostMapping("/worlds/{worldId}/events")
    public ResponseEntity<UUID> createEvent(
            @PathVariable UUID worldId,
            @Valid @RequestBody EventRequest request
    ) {
        var id = service.createEvent(worldId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @GetMapping("/worlds/{worldId}/events")
    public ResponseEntity<List<EventResponse>> getEventsByWorld(
            @PathVariable UUID worldId
    ) {
        return ResponseEntity.ok(service.getEventsByWorld(worldId));
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<EventResponse> getEventById(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(service.getEventById(id));
    }

    @PutMapping("/events/{id}")
    public ResponseEntity<Void> updateEvent(
            @PathVariable UUID id,
            @Valid @RequestBody EventRequest request
    ) {
        service.updateEvent(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/events/{id}")
    public ResponseEntity<Void> deleteEvent(
            @PathVariable UUID id
    ) {
        service.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}