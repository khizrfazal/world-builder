package com.my.worldbuilder.faction;

import com.my.worldbuilder.faction.dto.FactionRequest;
import com.my.worldbuilder.faction.dto.FactionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class FactionController {

    private final FactionService factionService;

    @PostMapping("/worlds/{worldId}/factions")
    public ResponseEntity<UUID> createFaction(
            @PathVariable UUID worldId,
            @Valid @RequestBody FactionRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());
        var id = factionService.createFaction(worldId, request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @GetMapping("/worlds/{worldId}/factions")
    public ResponseEntity<List<FactionResponse>> getFactionsByWorld(
            @PathVariable UUID worldId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(factionService.getFactionsByWorld(worldId, userId));
    }

    @GetMapping("/factions/{id}")
    public ResponseEntity<FactionResponse> getFactionById(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(factionService.getFactionById(id, userId));
    }

    @PutMapping("/factions/{id}")
    public ResponseEntity<Void> updateFaction(
            @PathVariable UUID id,
            @Valid @RequestBody FactionRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());
        factionService.updateFaction(id, request, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/factions/{id}")
    public ResponseEntity<Void> deleteFaction(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());
        factionService.deleteFaction(id, userId);
        return ResponseEntity.noContent().build();
    }
}
