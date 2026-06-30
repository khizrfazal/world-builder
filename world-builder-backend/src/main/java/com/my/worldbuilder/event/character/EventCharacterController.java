package com.my.worldbuilder.event.character;

import com.my.worldbuilder.event.character.dto.EventCharacterRequest;
import com.my.worldbuilder.event.character.dto.EventCharacterResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class EventCharacterController {

    private final EventCharacterService service;

    @PostMapping("/worlds/{worldId}/event-characters")
    public ResponseEntity<UUID> assignCharacterToEvent(
            @PathVariable UUID worldId,
            @Valid @RequestBody EventCharacterRequest request
    ) {
        var id = service.assignCharacterToEvent(worldId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @GetMapping("/worlds/{worldId}/events/{eventId}/characters")
    public ResponseEntity<List<EventCharacterResponse>> getCharactersForEvent(
            @PathVariable UUID worldId,
            @PathVariable UUID eventId
    ) {
        return ResponseEntity.ok(service.getCharactersForEvent(worldId, eventId));
    }

    @GetMapping("/event-characters/{id}")
    public ResponseEntity<EventCharacterResponse> getEventCharacterById(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(service.getEventCharacterById(id));
    }

    @PutMapping("/event-characters/{id}")
    public ResponseEntity<Void> updateEventCharacter(
            @PathVariable UUID id,
            @Valid @RequestBody EventCharacterRequest request
    ) {
        service.updateEventCharacter(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/event-characters/{id}")
    public ResponseEntity<Void> deleteEventCharacter(
            @PathVariable UUID id
    ) {
        service.deleteEventCharacter(id);
        return ResponseEntity.noContent().build();
    }
}