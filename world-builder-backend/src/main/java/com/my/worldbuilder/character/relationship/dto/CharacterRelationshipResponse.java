package com.my.worldbuilder.character.relationship.dto;

import com.my.worldbuilder.character.relationship.RelationshipType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class CharacterRelationshipResponse {
    private UUID id;
    private UUID fromCharacterId;
    private UUID toCharacterId;
    private RelationshipType type;
    private LocalDateTime createdAt;
}
