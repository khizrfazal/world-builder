package com.my.worldbuilder.common.exception;

import java.util.UUID;

public class LoreNotFoundException extends RuntimeException {

    public LoreNotFoundException(UUID id) {
        super("Lore entry " + id + " does not exist");
    }
}