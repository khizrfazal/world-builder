package com.my.worldbuilder.lore;

import com.my.worldbuilder.common.exception.ForbiddenException;
import com.my.worldbuilder.common.exception.LoreNotFoundException;
import com.my.worldbuilder.common.exception.WorldNotFoundException;
import com.my.worldbuilder.lore.dto.LoreRequest;
import com.my.worldbuilder.lore.dto.LoreResponse;
import com.my.worldbuilder.world.World;
import com.my.worldbuilder.world.WorldRepository;
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
    private final WorldRepository worldRepository;

    @Transactional
    public UUID createLore(UUID worldId, UUID userId, LoreRequest request) {
        World world = worldRepository.findById(worldId).orElseThrow(() -> new WorldNotFoundException(worldId));
        if (!world.getUser().getId().equals(userId)) {
            throw new ForbiddenException("World belongs to another user");
        }
        var loreEntry = loreMapper.toEntity(request);
        loreEntry.setWorld(world);
        return loreRepository.save(loreEntry).getId();
    }

    @Transactional(readOnly = true)
    public List<LoreResponse> getLores(UUID worldId, UUID userId) {
        World world = worldRepository.findById(worldId).orElseThrow(() -> new WorldNotFoundException(worldId));
        if (!world.getUser().getId().equals(userId)) {
            throw new ForbiddenException("World belongs to another user");
        }
        return loreRepository.findByWorld_Id(worldId)
                .stream()
                .map(loreMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public LoreResponse getLoreById(UUID id, UUID userId) {
        var lore = loreRepository.findById(id).orElseThrow(() -> new LoreNotFoundException(id));
        checkOwnership(lore, userId);
        return loreMapper.toResponse(lore);
    }

    @Transactional
    public void updateLore(UUID id, UUID userId, LoreRequest request) {
        var lore = loreRepository.findById(id).orElseThrow(() -> new LoreNotFoundException(id));
        checkOwnership(lore, userId);
        loreMapper.updateEntity(lore, request);
    }

    @Transactional
    public void deleteLore(UUID id, UUID userId) {
        var lore = loreRepository.findById(id).orElseThrow(() -> new LoreNotFoundException(id));
        checkOwnership(lore, userId);
        loreRepository.delete(lore);
    }

    private void checkOwnership(LoreEntry lore, UUID userId) {
        if (!lore.getWorld().getUser().getId().equals(userId)) {
            throw new ForbiddenException("World belongs to another user");
        }
    }
}