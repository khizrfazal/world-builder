package com.my.worldbuilder.faction;

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

    @Transactional
    public UUID createFaction(UUID worldId, FactionRequest request) {
        var factionEntity = factionMapper.toEntity(request);
        factionEntity.setWorldId(worldId);
        return factionRepository.save(factionEntity).getId();
    }

    public List<FactionResponse> getFactionsByWorld(UUID worldId) {
        return factionRepository.findByWorldId(worldId)
                .stream()
                .map(factionMapper::toResponse)
                .toList();
    }

    public FactionResponse getFactionById(UUID id) {
        return factionRepository.findById(id)
                .map(factionMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Faction not found"));
    }

    @Transactional
    public void updateFaction(UUID id, FactionRequest request) {
        var factionEntity = factionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Faction not found"));
        factionMapper.updateEntityFromRequest(request, factionEntity);
        factionRepository.save(factionEntity);
    }

    @Transactional
    public void deleteFaction(UUID id) {
        factionRepository.deleteById(id);
    }
}