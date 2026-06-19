package com.my.worldbuilder.relationship;

import com.my.worldbuilder.relationship.dto.RelationshipRequest;
import com.my.worldbuilder.relationship.dto.RelationshipResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class RelationshipController {
    private final RelationshipService relationshipService;

    @PostMapping("/worlds/{worldId}/relationships")
    public ResponseEntity<UUID> createRelationship(
            @PathVariable UUID worldId,
            @Valid @RequestBody RelationshipRequest request
    ) {
       var id = relationshipService.createRelationship(worldId, request);
       return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @DeleteMapping("/relationships/{id}")
    public ResponseEntity<Void> deleteRelationship(@PathVariable UUID id) {
        relationshipService.deleteRelationship(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/worlds/{worldId}/relationships")
    public ResponseEntity<List<RelationshipResponse>> getRelationshipsByWorld(@PathVariable UUID worldId) {
        return ResponseEntity.ok(relationshipService.getRelationships(worldId));
    }
}
