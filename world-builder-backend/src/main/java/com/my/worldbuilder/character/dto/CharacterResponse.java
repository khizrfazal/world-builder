package com.my.worldbuilder.character.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class CharacterResponse {
    private UUID id;
    private UUID worldId;
    private String name;
    private String summary;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
