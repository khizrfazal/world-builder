package com.my.worldbuilder.relationship;

import com.my.worldbuilder.relationship.dto.RelationshipRequest;
import com.my.worldbuilder.relationship.dto.RelationshipResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RelationshipService {
    private final RelationshipRepository relationshipRepository;
    private final RelationshipMapper relationshipMapper;

    @Transactional(readOnly = true)
    public List<RelationshipResponse> getRelationships(UUID worldId) {
        return relationshipRepository.findByWorldId(worldId)
                .stream()
                .map(relationshipMapper::toResponse)
                .toList();
    }

    @Transactional
    public UUID createRelationship(UUID worldId, RelationshipRequest request) {
        var relationship = relationshipMapper.toEntity(request);
        relationship.setWorldId(worldId);
        return relationshipRepository.save(relationship).getId();
    }

    @Transactional
    public void deleteRelationship(UUID id) {
        var relationship = relationshipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Relationship does not exist"));
        relationshipRepository.delete(relationship);
    }
}
