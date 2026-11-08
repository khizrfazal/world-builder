
package com.my.worldbuilder.faction;

import com.my.worldbuilder.common.exception.FactionNotFoundException;
import com.my.worldbuilder.common.security.WorldOwnershipService;
import com.my.worldbuilder.faction.dto.FactionRequest;
import com.my.worldbuilder.faction.dto.FactionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FactionService {

    private final FactionRepository factionRepository;
    private final FactionMapper factionMapper;
    private final WorldOwnershipService worldOwnershipService;

    @Transactional
    public UUID createFaction(UUID worldId, FactionRequest request, UUID userId) {
        var world = worldOwnershipService.getOwnedWorld(worldId, userId);
        var factionEntity = factionMapper.toEntity(request);
        factionEntity.setWorld(world);
        return factionRepository.save(factionEntity).getId();
    }

    @Transactional(readOnly = true)
    public List<FactionResponse> getFactionsByWorld(UUID worldId, UUID userId) {
        worldOwnershipService.getOwnedWorld(worldId, userId);
        return factionRepository.findByWorld_Id(worldId)
                .stream()
                .map(factionMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public FactionResponse getFactionById(UUID id, UUID userId) {
        var factionEntity = factionRepository.findById(id).orElseThrow(() -> new FactionNotFoundException(id));
        worldOwnershipService.assertOwnership(factionEntity.getWorld(), userId);
        return factionMapper.toResponse(factionEntity);
    }

    @Transactional
    public void updateFaction(UUID id, FactionRequest request, UUID userId) {
        var factionEntity = factionRepository.findById(id).orElseThrow(() -> new FactionNotFoundException(id));
        worldOwnershipService.assertOwnership(factionEntity.getWorld(), userId);
        factionMapper.updateEntityFromRequest(request, factionEntity);
    }

    @Transactional
    public void deleteFaction(UUID id, UUID userId) {
        var factionEntity = factionRepository.findById(id).orElseThrow(() -> new FactionNotFoundException(id));
        worldOwnershipService.assertOwnership(factionEntity.getWorld(), userId);
        factionRepository.delete(factionEntity);
    }
}