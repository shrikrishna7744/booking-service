package com.booking.exception;

public class EntityNotFound extends RuntimeException {

    public EntityNotFound(String message) {
        super(message);
    }
}
