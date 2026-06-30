package com.my.worldbuilder.character.relationship;

import com.my.worldbuilder.character.relationship.dto.CharacterRelationshipRequest;
import com.my.worldbuilder.character.relationship.dto.CharacterRelationshipResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CharacterRelationshipService {

    private final CharacterRelationshipRepository characterRelationshipRepository;
    private final CharacterRelationshipMapper characterRelationshipMapper;

    @Transactional
    public UUID createRelationship(UUID worldId, UUID characterId, CharacterRelationshipRequest request) {
        var characterRelationshipEntity = characterRelationshipMapper.toEntity(request);
        characterRelationshipEntity.setWorldId(worldId);
        characterRelationshipEntity.setFromCharacterId(characterId);
        return characterRelationshipRepository.save(characterRelationshipEntity).getId();
    }

    public List<CharacterRelationshipResponse> getRelationshipsForCharacter(UUID worldId, UUID characterId) {
        return characterRelationshipRepository.findByWorldIdAndFromCharacterId(worldId, characterId)
                .stream()
                .map(characterRelationshipMapper::toResponse)
                .toList();
    }

    @Transactional
    public void removeRelationship(UUID worldId, UUID relationshipId) {
        var rel = characterRelationshipRepository.findById(relationshipId)
                .orElseThrow(() -> new RuntimeException("Relationship not found"));
        if (!rel.getWorldId().equals(worldId)) {
            throw new RuntimeException("Forbidden");
        }
        characterRelationshipRepository.delete(rel);
    }
}
