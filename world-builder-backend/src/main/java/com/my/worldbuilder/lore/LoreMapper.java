package com.my.worldbuilder.lore;

import com.my.worldbuilder.common.mapping.EntityMapper;
import com.my.worldbuilder.lore.dto.LoreRequest;
import com.my.worldbuilder.lore.dto.LoreResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoreMapper extends EntityMapper<LoreEntry, LoreRequest> {
    LoreResponse toResponse(LoreEntry lore);
}