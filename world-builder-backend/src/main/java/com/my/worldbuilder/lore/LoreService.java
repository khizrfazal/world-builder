package com.my.worldbuilder.lore;

import com.my.worldbuilder.lore.dto.LoreRequest;
import com.my.worldbuilder.lore.dto.LoreResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class LoreService {

    private final LoreRepository loreRepository;
    private final LoreMapper loreMapper;

    @Transactional
    public UUID createLore(UUID worldId, LoreRequest request) {
        var loreEntry = loreMapper.toEntity(request);
        loreEntry.setWorldId(worldId);
        return loreRepository.save(loreEntry).getId();
    }

    @Transactional(readOnly = true)
    public List<LoreResponse> getLores(UUID worldId) {
        return loreRepository.findByWorldId(worldId)
                .stream()
                .map(loreMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public LoreResponse getLoreById(UUID id) {
        var lore = loreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lore entry does not exist"));
        return loreMapper.toResponse(lore);
    }

    @Transactional
    public void updateLore(UUID id, LoreRequest request) {
        var lore = loreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lore entry does not exist"));
        loreMapper.updateEntity(lore, request);
    }

    @Transactional
    public void deleteLore(UUID id) {
        var lore = loreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lore entry does not exist"));
        loreRepository.delete(lore);
    }
}