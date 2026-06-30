package com.my.worldbuilder.location;

import com.my.worldbuilder.common.mapping.EntityMapper;
import com.my.worldbuilder.location.dto.LocationRequest;
import com.my.worldbuilder.location.dto.LocationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LocationMapper extends EntityMapper<Location, LocationRequest> {
    LocationResponse toResponse(Location location);
    void updateEntityFromRequest(LocationRequest request, @MappingTarget Location entity);
}