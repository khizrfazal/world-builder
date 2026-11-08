
package com.my.worldbuilder.lore;

import com.my.worldbuilder.common.exception.LoreNotFoundException;
import com.my.worldbuilder.common.security.WorldOwnershipService;
import com.my.worldbuilder.lore.dto.LoreRequest;
import com.my.worldbuilder.lore.dto.LoreResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoreService {

    private final LoreRepository loreRepository;
    private final LoreMapper loreMapper;
    private final WorldOwnershipService worldOwnershipService;

    @Transactional
    public UUID createLore(UUID worldId, UUID userId, LoreRequest request) {
        var world = worldOwnershipService.getOwnedWorld(worldId, userId);
        var loreEntry = loreMapper.toEntity(request);
        loreEntry.setWorld(world);
        return loreRepository.save(loreEntry).getId();
    }

    @Transactional(readOnly = true)
    public List<LoreResponse> getLores(UUID worldId, UUID userId) {
        worldOwnershipService.getOwnedWorld(worldId, userId);
        return loreRepository.findByWorld_Id(worldId)
                .stream()
                .map(loreMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public LoreResponse getLoreById(UUID id, UUID userId) {
        var lore = loreRepository.findById(id).orElseThrow(() -> new LoreNotFoundException(id));
        worldOwnershipService.assertOwnership(lore.getWorld(), userId);
        return loreMapper.toResponse(lore);
    }

    @Transactional
    public void updateLore(UUID id, UUID userId, LoreRequest request) {
        var lore = loreRepository.findById(id).orElseThrow(() -> new LoreNotFoundException(id));
        worldOwnershipService.assertOwnership(lore.getWorld(), userId);
        loreMapper.updateEntity(lore, request);
    }

    @Transactional
    public void deleteLore(UUID id, UUID userId) {
        var lore = loreRepository.findById(id).orElseThrow(() -> new LoreNotFoundException(id));
        worldOwnershipService.assertOwnership(lore.getWorld(), userId);
        loreRepository.delete(lore);
    }
}