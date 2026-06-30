package com.my.worldbuilder.faction.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.my.worldbuilder.faction.Alignment;
import com.my.worldbuilder.faction.FactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FactionRequest {
    private String name;
    private String description;
    private Alignment alignment;
    private FactionType type;
}
