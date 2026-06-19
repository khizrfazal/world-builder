package com.my.worldbuilder.relationship;

import com.my.worldbuilder.common.mapping.EntityMapper;
import com.my.worldbuilder.relationship.dto.RelationshipRequest;
import com.my.worldbuilder.relationship.dto.RelationshipResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RelationshipMapper extends EntityMapper<Relationship, RelationshipRequest> {
    Relationship toEntity(RelationshipRequest request);
    RelationshipResponse toResponse(Relationship world);

}