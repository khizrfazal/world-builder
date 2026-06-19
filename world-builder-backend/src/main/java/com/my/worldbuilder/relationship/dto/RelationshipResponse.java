package com.my.worldbuilder.relationship.dto;

import com.my.worldbuilder.relationship.RelationshipType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelationshipResponse {
    private UUID id;
    private UUID worldId;
    private UUID sourceCharacterId;
    private UUID targetCharacterId;
    private RelationshipType relationshipType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}