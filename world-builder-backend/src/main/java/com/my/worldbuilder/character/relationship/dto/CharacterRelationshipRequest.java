package com.my.worldbuilder.character.relationship.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.my.worldbuilder.character.relationship.RelationshipType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CharacterRelationshipRequest {
    private UUID fromCharacterId;
    private UUID toCharacterId;
    private RelationshipType type;
}
