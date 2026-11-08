package com.my.worldbuilder.common.exception;

import java.util.UUID;

public class LocationNotFoundException extends RuntimeException {

    public LocationNotFoundException(UUID id) {
        super("Location " + id + " does not exist");
    }
}