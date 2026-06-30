package com.my.worldbuilder.character.relationship;

import com.my.worldbuilder.character.relationship.dto.CharacterRelationshipRequest;
import com.my.worldbuilder.character.relationship.dto.CharacterRelationshipResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CharacterRelationshipController {

    private final CharacterRelationshipService characterRelationshipService;

    @PostMapping("/worlds/{worldId}/characters/{characterId}/relationships")
    public ResponseEntity<UUID> addRelationship(
            @PathVariable UUID worldId,
            @PathVariable UUID characterId,
            @Valid @RequestBody CharacterRelationshipRequest request
    ) {
        var id = characterRelationshipService.createRelationship(worldId, characterId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @GetMapping("/worlds/{worldId}/characters/{characterId}/relationships")
    public ResponseEntity<List<CharacterRelationshipResponse>> getRelationships(
            @PathVariable UUID worldId,
            @PathVariable UUID characterId
    ) {
        var list = characterRelationshipService.getRelationshipsForCharacter(worldId, characterId);
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/worlds/{worldId}/relationships/{relationshipId}")
    public ResponseEntity<Void> deleteRelationship(
            @PathVariable UUID worldId,
            @PathVariable UUID relationshipId
    ) {
        characterRelationshipService.removeRelationship(worldId, relationshipId);
        return ResponseEntity.noContent().build();
    }
}
