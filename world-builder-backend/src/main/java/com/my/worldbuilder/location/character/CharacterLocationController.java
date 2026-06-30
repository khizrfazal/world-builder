package com.my.worldbuilder.location.character;

import com.my.worldbuilder.location.character.dto.CharacterLocationRequest;
import com.my.worldbuilder.location.character.dto.CharacterLocationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CharacterLocationController {

    private final CharacterLocationService service;

    @PostMapping("/worlds/{worldId}/character-locations")
    public ResponseEntity<UUID> assignCharacterToLocation(
            @PathVariable UUID worldId,
            @Valid @RequestBody CharacterLocationRequest request
    ) {
        var id = service.assignCharacterToLocation(worldId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @GetMapping("/worlds/{worldId}/characters/{characterId}/locations")
    public ResponseEntity<List<CharacterLocationResponse>> getLocationsForCharacter(
            @PathVariable UUID worldId,
            @PathVariable UUID characterId
    ) {
        var list = service.getLocationsForCharacter(worldId, characterId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/character-locations/{id}")
    public ResponseEntity<CharacterLocationResponse> getCharacterLocationById(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(service.getCharacterLocationById(id));
    }

    @PutMapping("/character-locations/{id}")
    public ResponseEntity<Void> updateCharacterLocation(
            @PathVariable UUID id,
            @Valid @RequestBody CharacterLocationRequest request
    ) {
        service.updateCharacterLocation(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/character-locations/{id}")
    public ResponseEntity<Void> deleteCharacterLocation(
            @PathVariable UUID id
    ) {
        service.deleteCharacterLocation(id);
        return ResponseEntity.noContent().build();
    }
}