package com.my.worldbuilder.character;

import com.my.worldbuilder.character.dto.CharacterRequest;
import com.my.worldbuilder.character.dto.CharacterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CharacterService {

    private final CharacterRepository characterRepository;
    private final CharacterMapper characterMapper;

    @Transactional
    public UUID createCharacter(UUID worldId, CharacterRequest request) {
        var character = characterMapper.toEntity(request);
        character.setWorldId(worldId);
        return characterRepository.save(character).getId();
    }

    @Transactional(readOnly = true)
    public List<CharacterResponse> getCharactersByWorld(UUID worldId) {
        return characterRepository.findByWorldId(worldId)
                .stream()
                .map(characterMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CharacterResponse getCharacterById(UUID id) {
        var character = characterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Character does not exist"));

        return characterMapper.toResponse(character);
    }

    @Transactional
    public void updateCharacter(UUID id, CharacterRequest request) {
        var character = characterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Character does not exist"));

        characterMapper.updateEntity(character, request);
    }

    @Transactional
    public void deleteCharacter(UUID id) {
        var character = characterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Character does not exist"));

        characterRepository.delete(character);
    }
}