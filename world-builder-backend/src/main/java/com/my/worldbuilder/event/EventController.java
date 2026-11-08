package com.my.worldbuilder.event;

import com.my.worldbuilder.event.dto.EventRequest;
import com.my.worldbuilder.event.dto.EventResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping("/worlds/{worldId}/events")
    public ResponseEntity<UUID> createEvent(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID worldId,
            @Valid @RequestBody EventRequest request
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());
        var id = eventService.createEvent(worldId, request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @GetMapping("/worlds/{worldId}/events")
    public ResponseEntity<List<EventResponse>> getEventsByWorld(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID worldId
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(
                eventService.getEventsByWorld(worldId, userId)
        );
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<EventResponse> getEventById(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID id
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(
                eventService.getEventById(id, userId)
        );
    }

    @PutMapping("/events/{id}")
    public ResponseEntity<Void> updateEvent(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID id,
            @Valid @RequestBody EventRequest request
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());
        eventService.updateEvent(id, request, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/events/{id}")
    public ResponseEntity<Void> deleteEvent(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID id
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());
        eventService.deleteEvent(id, userId);
        return ResponseEntity.noContent().build();
    }
}