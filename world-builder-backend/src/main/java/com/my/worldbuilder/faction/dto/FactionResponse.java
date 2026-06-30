package com.my.worldbuilder.faction.dto;

import com.my.worldbuilder.faction.Alignment;
import com.my.worldbuilder.faction.FactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class FactionResponse {
    private UUID id;
    private String name;
    private String description;
    private Alignment alignment;
    private FactionType type;
}
