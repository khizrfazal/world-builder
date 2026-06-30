package com.my.worldbuilder.location.character;

import com.my.worldbuilder.common.mapping.EntityMapper;
import com.my.worldbuilder.location.character.dto.CharacterLocationRequest;
import com.my.worldbuilder.location.character.dto.CharacterLocationResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CharacterLocationMapper
        extends EntityMapper<CharacterLocation, CharacterLocationRequest> {

    CharacterLocationResponse toResponse(CharacterLocation entity);
}
