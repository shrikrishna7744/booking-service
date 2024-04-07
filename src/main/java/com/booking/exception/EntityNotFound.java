package com.booking.exception;

public class EntityNotFound extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EntityNotFound(String message) {
        super(message);
    }

}
