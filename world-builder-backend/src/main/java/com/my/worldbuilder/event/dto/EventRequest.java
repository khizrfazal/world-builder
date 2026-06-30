package com.my.worldbuilder.event.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.my.worldbuilder.event.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventRequest {
    private String title;
    private String description;
    private LocalDate date;
    private EventType type;
}
