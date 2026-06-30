package com.my.worldbuilder.location.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class LocationResponse {
        private UUID id;
        private String name;
        private String description;
}
