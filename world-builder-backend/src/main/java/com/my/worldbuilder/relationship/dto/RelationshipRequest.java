package com.my.worldbuilder.relationship.dto;

import com.my.worldbuilder.relationship.RelationshipType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelationshipRequest {

    @NotNull
    private UUID sourceCharacterId;

    @NotNull
    private UUID targetCharacterId;

    @NotNull
    private RelationshipType relationshipType;
}