package com.my.worldbuilder.world;

import com.my.worldbuilder.world.dto.WorldRequest;
import com.my.worldbuilder.world.dto.WorldResponse;
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
@RequestMapping("/worlds")
@RequiredArgsConstructor
public class WorldController {
    private final WorldService worldService;

    @PostMapping
    public ResponseEntity<UUID> createWorld(@Valid @RequestBody WorldRequest world) {
        var worldId = worldService.createWorld(world);
        return ResponseEntity.status(HttpStatus.CREATED).body(worldId);
    }

    @GetMapping
    public ResponseEntity<List<WorldResponse>> getAllWorlds() {
        var worlds = worldService.getAllWorlds();
        return ResponseEntity.ok(worlds);
    }

    @GetMapping("/{worldId}")
    public ResponseEntity<WorldResponse> getWorldById(@PathVariable UUID worldId) {
        var world = worldService.getWorldById(worldId);
        return ResponseEntity.ok(world);
    }

    @PutMapping("/{worldId}")
    public ResponseEntity<Void> updateWorld(@PathVariable UUID worldId, @Valid @RequestBody WorldRequest world) {
        worldService.updateWorld(worldId, world);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{worldId}")
    public ResponseEntity<Void> deleteWorld(@PathVariable UUID worldId) {
        worldService.deleteWorld(worldId);
        return ResponseEntity.noContent().build();
    }
}
