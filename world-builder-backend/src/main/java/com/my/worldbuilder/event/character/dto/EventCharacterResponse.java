package com.my.worldbuilder.event.character.dto;

import com.my.worldbuilder.event.character.EventCharacterRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class EventCharacterResponse {
    private UUID id;
    private UUID eventId;
    private UUID characterId;
    private EventCharacterRole role;
}