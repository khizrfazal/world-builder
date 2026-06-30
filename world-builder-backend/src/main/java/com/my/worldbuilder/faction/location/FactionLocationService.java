package com.my.worldbuilder.faction.location;

import com.my.worldbuilder.faction.location.dto.FactionLocationRequest;
import com.my.worldbuilder.faction.location.dto.FactionLocationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FactionLocationService {

    private final FactionLocationRepository factionLocationRepository;
    private final FactionLocationMapper factionLocationMapper;

    @Transactional
    public UUID assignFactionToLocation(UUID worldId, FactionLocationRequest request) {
        var factionLocationEntity = factionLocationMapper.toEntity(request);
        factionLocationEntity.setWorldId(worldId);
        return factionLocationRepository.save(factionLocationEntity).getId();
    }

    public List<FactionLocationResponse> getLocationsForFaction(UUID worldId, UUID factionId) {
        return factionLocationRepository.findByWorldIdAndFactionId(worldId, factionId)
                .stream()
                .map(factionLocationMapper::toResponse)
                .toList();
    }

    public FactionLocationResponse getFactionLocationById(UUID id) {
        return factionLocationRepository.findById(id)
                .map(factionLocationMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("FactionLocation not found"));
    }

    @Transactional
    public void updateFactionLocation(UUID id, FactionLocationRequest request) {
        var entity = factionLocationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FactionLocation not found"));

        factionLocationMapper.updateEntityFromRequest(request, entity);
        factionLocationRepository.save(entity);
    }

    @Transactional
    public void deleteFactionLocation(UUID id) {
        factionLocationRepository.deleteById(id);
    }
}
