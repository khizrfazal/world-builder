package com.my.worldbuilder.common.exception;

import java.util.UUID;

public class CharacterNotFoundException extends RuntimeException {

    public CharacterNotFoundException(UUID id) {
        super("Character " + id + " does not exist");
    }
}