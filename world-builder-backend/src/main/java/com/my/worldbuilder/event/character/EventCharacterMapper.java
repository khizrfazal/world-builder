package com.my.worldbuilder.event.character;

import com.my.worldbuilder.common.mapping.EntityMapper;
import com.my.worldbuilder.event.character.dto.EventCharacterRequest;
import com.my.worldbuilder.event.character.dto.EventCharacterResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EventCharacterMapper extends EntityMapper<EventCharacter, EventCharacterRequest> {
    EventCharacterResponse toResponse(EventCharacter entity);
    void updateEntityFromRequest(EventCharacterRequest request, @MappingTarget EventCharacter entity);
}