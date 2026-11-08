package com.my.worldbuilder.faction.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.my.worldbuilder.faction.Alignment;
import com.my.worldbuilder.faction.FactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FactionRequest {

    @NotBlank(message = "Name must not be blank")
    @Size(max = 150, message = "Name must be less than 150 characters")
    private String name;

    @NotBlank(message = "Description must not be blank")
    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;

    @NotNull(message = "Alignment must not be null")
    private Alignment alignment;

    @NotNull(message = "Type must not be null")
    private FactionType type;
}