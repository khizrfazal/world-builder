package com.my.worldbuilder.character;

import com.my.worldbuilder.character.dto.CharacterRequest;
import com.my.worldbuilder.character.dto.CharacterResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class CharacterController {

    private final CharacterService characterService;

    @PostMapping("/worlds/{worldId}/characters")
    public ResponseEntity<UUID> createCharacter(
            @PathVariable UUID worldId,
            @Valid @RequestBody CharacterRequest request
    ) {
        var id = characterService.createCharacter(worldId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @GetMapping("/worlds/{worldId}/characters")
    public ResponseEntity<List<CharacterResponse>> getCharactersByWorld(
            @PathVariable UUID worldId
    ) {
        return ResponseEntity.ok(characterService.getCharactersByWorld(worldId));
    }

    @GetMapping("/characters/{id}")
    public ResponseEntity<CharacterResponse> getCharacterById(@PathVariable UUID id) {
        return ResponseEntity.ok(characterService.getCharacterById(id));
    }

    @PutMapping("/characters/{id}")
    public ResponseEntity<Void> updateCharacter(
            @PathVariable UUID id,
            @Valid @RequestBody CharacterRequest request
    ) {
        characterService.updateCharacter(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/characters/{id}")
    public ResponseEntity<Void> deleteCharacter(@PathVariable UUID id) {
        characterService.deleteCharacter(id);
        return ResponseEntity.noContent().build();
    }
}