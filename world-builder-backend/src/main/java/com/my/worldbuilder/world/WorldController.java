package com.my.worldbuilder.world;

import com.my.worldbuilder.world.dto.WorldRequest;
import com.my.worldbuilder.world.dto.WorldResponse;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/worlds")
@RequiredArgsConstructor
public class WorldController {
    private final WorldService worldService;

    @PostMapping
    public ResponseEntity<UUID> createWorld(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody WorldRequest world
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());
        UUID worldId = worldService.createWorld(world, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(worldId);
    }

    @GetMapping
    public ResponseEntity<List<WorldResponse>> getWorlds(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        var worlds = worldService.getWorldsForUser(userId);
        return ResponseEntity.ok(worlds);
    }

    @GetMapping("/{worldId}")
    public ResponseEntity<WorldResponse> getWorldById(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID worldId) {
        UUID userId = UUID.fromString(jwt.getSubject());
        var world = worldService.getWorldById(worldId, userId);
        return ResponseEntity.ok(world);
    }

    @PutMapping("/{worldId}")
    public ResponseEntity<Void> updateWorld(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID worldId, @Valid @RequestBody WorldRequest world) {
        UUID userId = UUID.fromString(jwt.getSubject());
        worldService.updateWorld(worldId, world, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{worldId}")
    public ResponseEntity<Void> deleteWorld(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID worldId) {
        UUID userId = UUID.fromString(jwt.getSubject());
        worldService.deleteWorld(worldId, userId);
        return ResponseEntity.noContent().build();
    }
}
