package com.my.worldbuilder.character;

import com.my.worldbuilder.character.dto.CharacterRequest;
import com.my.worldbuilder.character.dto.CharacterResponse;
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

    @Transactional
    public UUID createCharacter(UUID worldId, CharacterRequest request) {
        var characterEntity = characterMapper.toEntity(request);
        characterEntity.setWorldId(worldId);
        return characterRepository.save(characterEntity).getId();
    }

    public List<CharacterResponse> getCharactersByWorld(UUID worldId) {
        return characterRepository.findByWorldId(worldId)
                .stream()
                .map(characterMapper::toResponse)
                .toList();
    }

    public CharacterResponse getCharacterById(UUID id) {
        var characterEntity = characterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Character does not exist"));
        return characterMapper.toResponse(characterEntity);
    }

    @Transactional
    public void updateCharacter(UUID id, CharacterRequest request) {
        var characterEntity = characterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Character does not exist"));
        characterMapper.updateEntity(characterEntity, request);
    }

    @Transactional
    public void deleteCharacter(UUID id) {
        var characterEntity = characterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Character does not exist"));
        characterRepository.delete(characterEntity);
    }
}