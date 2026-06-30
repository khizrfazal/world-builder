package com.my.worldbuilder.event;

import com.my.worldbuilder.common.mapping.EntityMapper;
import com.my.worldbuilder.event.dto.EventRequest;
import com.my.worldbuilder.event.dto.EventResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EventMapper extends EntityMapper<Event, EventRequest> {
    EventResponse toResponse(Event entity);
    void updateEntityFromRequest(EventRequest request, @MappingTarget Event entity);
}