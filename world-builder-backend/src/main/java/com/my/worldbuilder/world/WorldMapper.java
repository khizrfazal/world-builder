package com.my.worldbuilder.world;

import com.my.worldbuilder.common.mapping.EntityMapper;
import com.my.worldbuilder.world.dto.WorldRequest;
import com.my.worldbuilder.world.dto.WorldResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WorldMapper extends EntityMapper<World, WorldRequest> {

    World toEntity(WorldRequest request);

    WorldResponse toResponse(World world);

}