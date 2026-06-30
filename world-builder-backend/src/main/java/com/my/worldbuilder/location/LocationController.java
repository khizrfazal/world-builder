package com.my.worldbuilder.location;

import com.my.worldbuilder.location.dto.LocationRequest;
import com.my.worldbuilder.location.dto.LocationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            @Valid @RequestBody LocationRequest request
    ) {
        var id = locationService.createLocation(worldId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @GetMapping("/worlds/{worldId}/locations")
    public ResponseEntity<List<LocationResponse>> getLocationsByWorld(
            @PathVariable UUID worldId
    ) {
        var locations = locationService.getLocationsByWorld(worldId);
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/locations/{id}")
    public ResponseEntity<LocationResponse> getLocationById(
            @PathVariable UUID id
    ) {
        var response = locationService.getLocationById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/locations/{id}")
    public ResponseEntity<Void> updateLocation(
            @PathVariable UUID id,
            @Valid @RequestBody LocationRequest request
    ) {
        locationService.updateLocation(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/locations/{id}")
    public ResponseEntity<Void> deleteLocation(
            @PathVariable UUID id
    ) {
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }
}
