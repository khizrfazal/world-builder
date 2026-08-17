package com.my.worldbuilder.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private UUID id;
    private String username;
    private String token;
}