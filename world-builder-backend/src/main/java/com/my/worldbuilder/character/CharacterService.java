package com.my.worldbuilder.character;

import com.my.worldbuilder.character.dto.CharacterRequest;
import com.my.worldbuilder.character.dto.CharacterResponse;
import com.my.worldbuilder.common.exception.CharacterNotFoundException;
import com.my.worldbuilder.common.exception.ForbiddenException;
import com.my.worldbuilder.world.WorldRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CharacterService {

    private final CharacterRepository characterRepository;
    private final CharacterMapper characterMapper;
    private final WorldRepository worldRepository;

    private void assertOwnership(Character character, UUID userId) {
        if (!character.getWorld().getUser().getId().equals(userId)) {
            throw new ForbiddenException("Not your character");
        }
    }

    @Transactional
    public UUID createCharacter(UUID worldId, CharacterRequest request, UUID userId) {
        var world = worldRepository.findById(worldId)
                .orElseThrow(() -> new RuntimeException("World does not exist"));

        if (!world.getUser().getId().equals(userId)) {
            throw new ForbiddenException("Not your world");
        }

        var characterEntity = characterMapper.toEntity(request);
        characterEntity.setWorld(world);

        return characterRepository.save(characterEntity).getId();
    }

    public List<CharacterResponse> getCharactersByWorld(UUID worldId, UUID userId) {
        var world = worldRepository.findById(worldId)
                .orElseThrow(() -> new RuntimeException("World does not exist"));

        if (!world.getUser().getId().equals(userId)) {
            throw new ForbiddenException("Not your world");
        }

        return characterRepository.findByWorld_Id(worldId)
                .stream()
                .map(characterMapper::toResponse)
                .toList();
    }

    public CharacterResponse getCharacterById(UUID id, UUID userId) {
        var characterEntity = characterRepository.findById(id)
                .orElseThrow(() -> new CharacterNotFoundException(id));

        assertOwnership(characterEntity, userId);

        return characterMapper.toResponse(characterEntity);
    }

    @Transactional
    public void updateCharacter(UUID id, CharacterRequest request, UUID userId) {
        var characterEntity = characterRepository.findById(id)
                .orElseThrow(() -> new CharacterNotFoundException(id));

        assertOwnership(characterEntity, userId);

        characterMapper.updateEntity(characterEntity, request);
    }

    @Transactional
    public void deleteCharacter(UUID id, UUID userId) {
        var characterEntity = characterRepository.findById(id)
                .orElseThrow(() -> new CharacterNotFoundException(id));

        assertOwnership(characterEntity, userId);

        characterRepository.delete(characterEntity);
    }
}