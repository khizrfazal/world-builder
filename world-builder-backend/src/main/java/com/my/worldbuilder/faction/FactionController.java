package com.my.worldbuilder.faction;

import com.my.worldbuilder.faction.dto.FactionRequest;
import com.my.worldbuilder.faction.dto.FactionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            @Valid @RequestBody FactionRequest request
    ) {
        var id = factionService.createFaction(worldId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @GetMapping("/worlds/{worldId}/factions")
    public ResponseEntity<List<FactionResponse>> getFactionsByWorld(
            @PathVariable UUID worldId
    ) {
        return ResponseEntity.ok(factionService.getFactionsByWorld(worldId));
    }

    @GetMapping("/factions/{id}")
    public ResponseEntity<FactionResponse> getFactionById(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(factionService.getFactionById(id));
    }

    @PutMapping("/factions/{id}")
    public ResponseEntity<Void> updateFaction(
            @PathVariable UUID id,
            @Valid @RequestBody FactionRequest request
    ) {
        factionService.updateFaction(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/factions/{id}")
    public ResponseEntity<Void> deleteFaction(
            @PathVariable UUID id
    ) {
        factionService.deleteFaction(id);
        return ResponseEntity.noContent().build();
    }
}