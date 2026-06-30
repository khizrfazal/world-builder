package com.my.worldbuilder.faction;

import com.my.worldbuilder.common.mapping.EntityMapper;
import com.my.worldbuilder.faction.dto.FactionRequest;
import com.my.worldbuilder.faction.dto.FactionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FactionMapper extends EntityMapper<Faction, FactionRequest> {
    FactionResponse toResponse(Faction entity);
    void updateEntityFromRequest(FactionRequest request, @MappingTarget Faction entity);
}