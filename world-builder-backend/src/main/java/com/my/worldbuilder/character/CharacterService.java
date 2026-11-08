
package com.my.worldbuilder.character;

import com.my.worldbuilder.character.dto.CharacterRequest;
import com.my.worldbuilder.character.dto.CharacterResponse;
import com.my.worldbuilder.common.exception.CharacterNotFoundException;
import com.my.worldbuilder.common.security.WorldOwnershipService;
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
    private final WorldOwnershipService worldOwnershipService;

    @Transactional
    public UUID createCharacter(UUID worldId, CharacterRequest request, UUID userId) {
        var world = worldOwnershipService.getOwnedWorld(worldId, userId);
        var characterEntity = characterMapper.toEntity(request);
        characterEntity.setWorld(world);
        return characterRepository.save(characterEntity).getId();
    }

    @Transactional(readOnly = true)
    public List<CharacterResponse> getCharactersByWorld(UUID worldId, UUID userId) {
        worldOwnershipService.getOwnedWorld(worldId, userId);
        return characterRepository.findByWorld_Id(worldId)
                .stream()
                .map(characterMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CharacterResponse getCharacterById(UUID id, UUID userId) {
        var characterEntity = characterRepository.findById(id).orElseThrow(() -> new CharacterNotFoundException(id));
        worldOwnershipService.assertOwnership(characterEntity.getWorld(), userId);
        return characterMapper.toResponse(characterEntity);
    }

    @Transactional
    public void updateCharacter(UUID id, CharacterRequest request, UUID userId) {
        var characterEntity = characterRepository.findById(id).orElseThrow(() -> new CharacterNotFoundException(id));
        worldOwnershipService.assertOwnership(characterEntity.getWorld(), userId);
        characterMapper.updateEntity(characterEntity, request);
    }

    @Transactional
    public void deleteCharacter(UUID id, UUID userId) {
        var characterEntity = characterRepository.findById(id).orElseThrow(() -> new CharacterNotFoundException(id));
        worldOwnershipService.assertOwnership(characterEntity.getWorld(), userId);
        characterRepository.delete(characterEntity);
    }
}