package com.my.worldbuilder.location.character;

import com.my.worldbuilder.location.character.dto.CharacterLocationRequest;
import com.my.worldbuilder.location.character.dto.CharacterLocationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CharacterLocationService {
    private final CharacterLocationRepository characterLocationRepository;
    private final CharacterLocationMapper characterLocationMapper;

    @Transactional
    public UUID assignCharacterToLocation(UUID worldId, CharacterLocationRequest request) {
        var characterLocationEntity = characterLocationMapper.toEntity(request);
        characterLocationEntity.setWorldId(worldId);
        return characterLocationRepository.save(characterLocationEntity).getId();
    }

    public List<CharacterLocationResponse> getLocationsForCharacter(UUID worldId, UUID characterId) {
        return characterLocationRepository.findByWorldIdAndCharacterId(worldId, characterId)
                .stream()
                .map(characterLocationMapper::toResponse)
                .toList();
    }

    public CharacterLocationResponse getCharacterLocationById(UUID id) {
        return characterLocationRepository.findById(id)
                .map(characterLocationMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("CharacterLocation not found"));
    }

    @Transactional
    public void updateCharacterLocation(UUID id, CharacterLocationRequest request) {
        var characterLocationEntity = characterLocationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CharacterLocation not found"));
        characterLocationMapper.updateEntity(characterLocationEntity, request);
        characterLocationRepository.save(characterLocationEntity);
    }

    @Transactional
    public void deleteCharacterLocation(UUID id) {
        characterLocationRepository.deleteById(id);
    }
}
