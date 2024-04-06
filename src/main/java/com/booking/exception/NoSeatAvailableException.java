package com.booking.exception;

public class NoSeatAvailableException extends RuntimeException {

    public NoSeatAvailableException(String message) {
        super(message);
    }
}
