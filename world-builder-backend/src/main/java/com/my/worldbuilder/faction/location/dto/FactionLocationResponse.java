package com.my.worldbuilder.faction.location.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class FactionLocationResponse {
    private UUID id;
    private UUID factionId;
    private UUID locationId;
    private String role;
}
