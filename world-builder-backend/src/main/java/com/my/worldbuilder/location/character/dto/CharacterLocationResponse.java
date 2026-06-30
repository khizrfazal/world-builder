package com.my.worldbuilder.location.character.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class CharacterLocationResponse {
    private UUID id;
    private UUID characterId;
    private UUID locationId;
}
