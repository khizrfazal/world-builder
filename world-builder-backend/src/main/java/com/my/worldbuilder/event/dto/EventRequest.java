package com.my.worldbuilder.event.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.my.worldbuilder.event.EventType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventRequest {

    @NotBlank(message = "Title must not be blank")
    @Size(max = 150, message = "Title must be less than 150 characters")
    private String title;

    @Size(max = 5000, message = "Description must be less than 5000 characters")
    private String description;

    private LocalDate date;

    private EventType type;
}
