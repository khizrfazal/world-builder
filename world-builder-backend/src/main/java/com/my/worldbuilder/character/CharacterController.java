package com.my.worldbuilder.character;

import com.my.worldbuilder.character.dto.CharacterRequest;
import com.my.worldbuilder.character.dto.CharacterResponse;
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
public class CharacterController {

    private final CharacterService characterService;

    @PostMapping("/worlds/{worldId}/characters")
    public ResponseEntity<UUID> createCharacter(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID worldId,
            @Valid @RequestBody CharacterRequest request
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());
        var id = characterService.createCharacter(worldId, request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @GetMapping("/worlds/{worldId}/characters")
    public ResponseEntity<List<CharacterResponse>> getCharactersByWorld(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID worldId
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(characterService.getCharactersByWorld(worldId, userId));
    }

    @GetMapping("/characters/{id}")
    public ResponseEntity<CharacterResponse> getCharacterById(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID id
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(characterService.getCharacterById(id, userId));
    }

    @PutMapping("/characters/{id}")
    public ResponseEntity<Void> updateCharacter(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID id,
            @Valid @RequestBody CharacterRequest request
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());
        characterService.updateCharacter(id, request, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/characters/{id}")
    public ResponseEntity<Void> deleteCharacter(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID id
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());
        characterService.deleteCharacter(id, userId);
        return ResponseEntity.noContent().build();
    }

}