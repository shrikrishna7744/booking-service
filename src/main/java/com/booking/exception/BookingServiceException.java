package com.booking.exception;

public class BookingServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BookingServiceException(String message) {
        super(message);
    }

}
