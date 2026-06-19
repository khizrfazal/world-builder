package com.my.worldbuilder.character.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharacterResponse {
    private UUID id;
    private UUID worldId;
    private String name;
    private String summary;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
