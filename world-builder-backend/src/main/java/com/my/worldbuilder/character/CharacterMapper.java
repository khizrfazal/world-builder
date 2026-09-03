package com.my.worldbuilder.character;

import com.my.worldbuilder.character.dto.CharacterRequest;
import com.my.worldbuilder.character.dto.CharacterResponse;
import com.my.worldbuilder.common.mapping.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CharacterMapper extends EntityMapper<Character, CharacterRequest> {
    @Override
    @Mapping(target = "world", ignore = true)
    Character toEntity(CharacterRequest request);

    @Mapping(target = "worldId", source = "world.id")
    CharacterResponse toResponse(Character character);
}
