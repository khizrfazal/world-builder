
package com.my.worldbuilder.location;

import com.my.worldbuilder.common.exception.LocationNotFoundException;
import com.my.worldbuilder.common.security.WorldOwnershipService;
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
    private final WorldOwnershipService worldOwnershipService;

    @Transactional
    public UUID createLocation(UUID worldId, LocationRequest request, UUID userId) {
        var world = worldOwnershipService.getOwnedWorld(worldId, userId);
        var entity = locationMapper.toEntity(request);
        entity.setWorld(world);
        return locationRepository.save(entity).getId();
    }

    @Transactional(readOnly = true)
    public List<LocationResponse> getLocationsByWorld(UUID worldId, UUID userId) {
        worldOwnershipService.getOwnedWorld(worldId, userId);
        return locationRepository.findByWorld_Id(worldId)
                .stream()
                .map(locationMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public LocationResponse getLocationById(UUID id, UUID userId) {
        var entity = locationRepository.findById(id).orElseThrow(() -> new LocationNotFoundException(id));
        worldOwnershipService.assertOwnership(entity.getWorld(), userId);
        return locationMapper.toResponse(entity);
    }

    @Transactional
    public void updateLocation(UUID id, LocationRequest request, UUID userId) {
        var entity = locationRepository.findById(id).orElseThrow(() -> new LocationNotFoundException(id));
        worldOwnershipService.assertOwnership(entity.getWorld(), userId);
        locationMapper.updateEntityFromRequest(request, entity);
    }

    @Transactional
    public void deleteLocation(UUID id, UUID userId) {
        var entity = locationRepository.findById(id).orElseThrow(() -> new LocationNotFoundException(id));
        worldOwnershipService.assertOwnership(entity.getWorld(), userId);
        locationRepository.delete(entity);
    }
}