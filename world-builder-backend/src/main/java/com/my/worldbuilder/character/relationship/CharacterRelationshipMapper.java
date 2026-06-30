package com.my.worldbuilder.character.relationship;

import com.my.worldbuilder.character.relationship.dto.CharacterRelationshipRequest;
import com.my.worldbuilder.character.relationship.dto.CharacterRelationshipResponse;
import com.my.worldbuilder.common.mapping.EntityMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CharacterRelationshipMapper extends EntityMapper<CharacterRelationship, CharacterRelationshipRequest> {
    CharacterRelationshipResponse toResponse(CharacterRelationship relationship);
}
