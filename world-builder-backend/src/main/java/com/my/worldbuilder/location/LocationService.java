package com.my.worldbuilder.location;

import com.my.worldbuilder.common.exception.ForbiddenException;
import com.my.worldbuilder.common.exception.LocationNotFoundException;
import com.my.worldbuilder.common.exception.WorldNotFoundException;
import com.my.worldbuilder.location.dto.LocationRequest;
import com.my.worldbuilder.location.dto.LocationResponse;
import com.my.worldbuilder.world.WorldRepository;
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
    private final WorldRepository worldRepository;

    private void assertOwnership(Location location, UUID userId) {
        if (!location.getWorld().getUser().getId().equals(userId)) {
            throw new ForbiddenException("Not your location");
        }
    }

    @Transactional
    public UUID createLocation(UUID worldId, LocationRequest request, UUID userId) {
        var world = worldRepository.findById(worldId).orElseThrow(() -> new WorldNotFoundException(worldId));
        if (!world.getUser().getId().equals(userId)) {
            throw new ForbiddenException("Not your world");
        }
        var entity = locationMapper.toEntity(request);
        entity.setWorld(world);
        return locationRepository.save(entity).getId();
    }

    @Transactional(readOnly = true)
    public List<LocationResponse> getLocationsByWorld(UUID worldId, UUID userId) {
        var world = worldRepository.findById(worldId).orElseThrow(() -> new WorldNotFoundException(worldId));
        if (!world.getUser().getId().equals(userId)) {
            throw new ForbiddenException("Not your world");
        }
        return locationRepository.findByWorld_Id(worldId)
                .stream()
                .map(locationMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public LocationResponse getLocationById(UUID id, UUID userId) {
        var entity = locationRepository.findById(id).orElseThrow(() -> new LocationNotFoundException(id));
        assertOwnership(entity, userId);
        return locationMapper.toResponse(entity);
    }

    @Transactional
    public void updateLocation(UUID id, LocationRequest request, UUID userId) {
        var entity = locationRepository.findById(id).orElseThrow(() -> new LocationNotFoundException(id));
        assertOwnership(entity, userId);
        locationMapper.updateEntityFromRequest(request, entity);
    }

    @Transactional
    public void deleteLocation(UUID id, UUID userId) {
        var entity = locationRepository.findById(id).orElseThrow(() -> new LocationNotFoundException(id));
        assertOwnership(entity, userId);
        locationRepository.delete(entity);
    }
}