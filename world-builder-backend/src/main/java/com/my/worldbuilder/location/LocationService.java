package com.my.worldbuilder.location;

import com.my.worldbuilder.location.dto.LocationRequest;
import com.my.worldbuilder.location.dto.LocationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    @Transactional
    public UUID createLocation(UUID worldId, LocationRequest request) {
        var entity = locationMapper.toEntity(request);
        entity.setWorldId(worldId);
        return locationRepository.save(entity).getId();
    }

    public List<LocationResponse> getLocationsByWorld(UUID worldId) {
        return locationRepository.findByWorldId(worldId)
                .stream()
                .map(locationMapper::toResponse)
                .toList();
    }

    public LocationResponse getLocationById(UUID id) {
        return locationRepository.findById(id)
                .map(locationMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Location not found"));
    }

    @Transactional
    public void updateLocation(UUID id, LocationRequest request) {
        var entity = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found"));
        locationMapper.updateEntityFromRequest(request, entity);
        locationRepository.save(entity);
    }

    @Transactional
    public void deleteLocation(UUID id) {
        locationRepository.deleteById(id);
    }
}
