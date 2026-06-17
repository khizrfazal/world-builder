package com.my.worldbuilder.world;

import com.my.worldbuilder.world.dto.WorldRequest;
import com.my.worldbuilder.world.dto.WorldResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class WorldService {

    private final WorldRepository worldRepository;
    private final WorldMapper worldMapper;

    @Transactional
    public UUID createWorld(WorldRequest request) {
        World worldEntity = worldMapper.toEntity(request);
        return worldRepository.save(worldEntity).getId();
    }

    @Transactional(readOnly = true)
    public List<WorldResponse> getAllWorlds() {
        return worldRepository.findAll()
                .stream()
                .map(worldMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public WorldResponse getWorldById(UUID id) {
        World world = worldRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("World does not exist"));
        return worldMapper.toResponse(world);
    }

    @Transactional
    public void updateWorld(UUID id, WorldRequest request) {
        var world = worldRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("World does not exist"));
        worldMapper.updateEntity(world, request);
    }

    @Transactional
    public void deleteWorld(UUID id) {
        var world = worldRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("World does not exist"));

        worldRepository.delete(world);
    }
}