package com.my.worldbuilder.common.exception;

import java.util.UUID;

public class WorldNotFoundException extends RuntimeException {

    public WorldNotFoundException(UUID id) {
        super("World " + id + " does not exist");
    }
}