
package com.my.worldbuilder.common.security;

import com.my.worldbuilder.common.exception.ForbiddenException;
import com.my.worldbuilder.common.exception.WorldNotFoundException;
import com.my.worldbuilder.world.World;
import com.my.worldbuilder.world.WorldRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorldOwnershipService {

    private final WorldRepository worldRepository;

    public World getOwnedWorld(UUID worldId, UUID userId) {
        World world = worldRepository.findById(worldId).orElseThrow(() -> new WorldNotFoundException(worldId));
        assertOwnership(world, userId);
        return world;
    }

    public void assertOwnership(World world, UUID userId) {
        if (!world.getUser().getId().equals(userId)) {
            throw new ForbiddenException("Not your world");
        }
    }
}