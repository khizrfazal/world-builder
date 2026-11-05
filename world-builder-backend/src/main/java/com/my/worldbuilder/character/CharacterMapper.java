package com.my.worldbuilder.character;

import com.my.worldbuilder.character.dto.CharacterRequest;
import com.my.worldbuilder.character.dto.CharacterResponse;
import com.my.worldbuilder.common.mapping.EntityMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CharacterMapper extends EntityMapper<Character, CharacterRequest> {
    CharacterResponse toResponse(Character character);
}
