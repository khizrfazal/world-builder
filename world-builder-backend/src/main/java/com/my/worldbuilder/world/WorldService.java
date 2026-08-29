package com.my.worldbuilder.world;

import com.my.worldbuilder.common.exception.ForbiddenException;
import com.my.worldbuilder.common.exception.WorldNotFoundException;
import com.my.worldbuilder.user.User;
import com.my.worldbuilder.user.UserRepository;
import com.my.worldbuilder.world.dto.WorldRequest;
import com.my.worldbuilder.world.dto.WorldResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorldService {

    private final WorldRepository worldRepository;
    private final WorldMapper worldMapper;
    private final UserRepository userRepository;

    @Transactional
    public UUID createWorld(WorldRequest request, UUID userId) {
        User user = userRepository.findById(userId).orElseThrow();
        World worldEntity = worldMapper.toEntity(request);
        worldEntity.touch(user);
        return worldRepository.save(worldEntity).getId();
    }

    public List<WorldResponse> getWorldsForUser(UUID userId) {
        return worldRepository.findAllByUserId(userId)
                .stream()
                .map(worldMapper::toResponse)
                .toList();
    }

    public WorldResponse getWorldById(UUID worldId, UUID userId) {
        World world = worldRepository.findById(worldId)
                .orElseThrow(() -> new WorldNotFoundException(worldId));

        if (!world.getUser().getId().equals(userId)) {
            throw new ForbiddenException("Not your world");
        }
        return worldMapper.toResponse(world);
    }

    @Transactional
    public void updateWorld(UUID worldId, WorldRequest request, UUID userId) {
        World world = worldRepository.findById(worldId)
                .orElseThrow(() -> new WorldNotFoundException(worldId));
        if (!world.getUser().getId().equals(userId)) {
            throw new ForbiddenException("Not your world");
        }
        worldMapper.updateEntity(world, request);
    }

    @Transactional
    public void deleteWorld(UUID worldId, UUID userId) {
        World world = worldRepository.findById(worldId)
                .orElseThrow(() -> new WorldNotFoundException(worldId));
        if (!world.getUser().getId().equals(userId)) {
             throw new ForbiddenException("Not your world");
        }
        worldRepository.delete(world);
    }
}