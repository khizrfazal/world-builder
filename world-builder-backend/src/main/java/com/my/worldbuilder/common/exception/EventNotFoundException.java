package com.my.worldbuilder.common.exception;

import java.util.UUID;

public class EventNotFoundException extends RuntimeException {

    public EventNotFoundException(UUID id) {
        super("Event " + id + " does not exist");
    }
}