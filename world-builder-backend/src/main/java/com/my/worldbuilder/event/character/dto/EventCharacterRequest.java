package com.my.worldbuilder.event.character.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.my.worldbuilder.event.character.EventCharacterRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventCharacterRequest {
    private UUID eventId;
    private UUID characterId;
    private EventCharacterRole role;
}