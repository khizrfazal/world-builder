package com.my.worldbuilder.faction.location;

import com.my.worldbuilder.common.mapping.EntityMapper;
import com.my.worldbuilder.faction.location.dto.FactionLocationRequest;
import com.my.worldbuilder.faction.location.dto.FactionLocationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FactionLocationMapper extends EntityMapper<FactionLocation, FactionLocationRequest> {
    FactionLocationResponse toResponse(FactionLocation entity);
    void updateEntityFromRequest(FactionLocationRequest request, @MappingTarget FactionLocation entity);
}
