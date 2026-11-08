package com.my.worldbuilder.location;

import com.my.worldbuilder.location.dto.LocationRequest;
import com.my.worldbuilder.location.dto.LocationResponse;
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
public class LocationController {

    private final LocationService locationService;

    @PostMapping("/worlds/{worldId}/locations")
    public ResponseEntity<UUID> createLocation(
            @PathVariable UUID worldId,
            @Valid @RequestBody LocationRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());
        var id = locationService.createLocation(worldId, request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @GetMapping("/worlds/{worldId}/locations")
    public ResponseEntity<List<LocationResponse>> getLocationsByWorld(
            @PathVariable UUID worldId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());
        var locations = locationService.getLocationsByWorld(worldId, userId);
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/locations/{id}")
    public ResponseEntity<LocationResponse> getLocationById(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());
        var response = locationService.getLocationById(id, userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/locations/{id}")
    public ResponseEntity<Void> updateLocation(
            @PathVariable UUID id,
            @Valid @RequestBody LocationRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());
        locationService.updateLocation(id, request, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/locations/{id}")
    public ResponseEntity<Void> deleteLocation(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());
        locationService.deleteLocation(id, userId);
        return ResponseEntity.noContent().build();
    }
}