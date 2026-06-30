package com.my.worldbuilder.lore;

import com.my.worldbuilder.lore.dto.LoreRequest;
import com.my.worldbuilder.lore.dto.LoreResponse;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class LoreController {

    private final LoreService loreService;

    @PostMapping("/worlds/{worldId}/lores")
    public ResponseEntity<UUID> createLore(
            @PathVariable UUID worldId,
            @Valid @RequestBody LoreRequest request
    ) {
        var id = loreService.createLore(worldId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @GetMapping("/worlds/{worldId}/lores")
    public ResponseEntity<List<LoreResponse>> getLores(
            @PathVariable UUID worldId
    ) {
        return ResponseEntity.ok(loreService.getLores(worldId));
    }

    @GetMapping("/lores/{id}")
    public ResponseEntity<LoreResponse> getLoreById(@PathVariable UUID id) {
        return ResponseEntity.ok(loreService.getLoreById(id));
    }

    @PutMapping("/lores/{id}")
    public ResponseEntity<Void> updateLore(
            @PathVariable UUID id,
            @Valid @RequestBody LoreRequest request
    ) {
        loreService.updateLore(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/lores/{id}")
    public ResponseEntity<Void> deleteLore(@PathVariable UUID id) {
        loreService.deleteLore(id);
        return ResponseEntity.noContent().build();
    }
}