package com.my.worldbuilder.common.exception;

import java.util.UUID;

public class FactionNotFoundException extends RuntimeException {

    public FactionNotFoundException(UUID id) {
        super("Faction " + id + " does not exist");
    }
}