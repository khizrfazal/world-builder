package com.my.worldbuilder.faction.location;

import com.my.worldbuilder.faction.location.dto.FactionLocationRequest;
import com.my.worldbuilder.faction.location.dto.FactionLocationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class FactionLocationController {

    private final FactionLocationService service;

    @PostMapping("/worlds/{worldId}/faction-locations")
    public ResponseEntity<UUID> assignFactionToLocation(
            @PathVariable UUID worldId,
            @Valid @RequestBody FactionLocationRequest request
    ) {
        var id = service.assignFactionToLocation(worldId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @GetMapping("/worlds/{worldId}/factions/{factionId}/locations")
    public ResponseEntity<List<FactionLocationResponse>> getLocationsForFaction(
            @PathVariable UUID worldId,
            @PathVariable UUID factionId
    ) {
        return ResponseEntity.ok(service.getLocationsForFaction(worldId, factionId));
    }

    @GetMapping("/faction-locations/{id}")
    public ResponseEntity<FactionLocationResponse> getFactionLocationById(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(service.getFactionLocationById(id));
    }

    @PutMapping("/faction-locations/{id}")
    public ResponseEntity<Void> updateFactionLocation(
            @PathVariable UUID id,
            @Valid @RequestBody FactionLocationRequest request
    ) {
        service.updateFactionLocation(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/faction-locations/{id}")
    public ResponseEntity<Void> deleteFactionLocation(
            @PathVariable UUID id
    ) {
        service.deleteFactionLocation(id);
        return ResponseEntity.noContent().build();
    }
}
