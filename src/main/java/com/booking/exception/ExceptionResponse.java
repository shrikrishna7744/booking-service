package com.booking.exception;

import org.springframework.util.StringUtils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionResponse {

    enum ErrorType {
        BOOKING_NOT_FOUND, NO_SEAT_AVAILABLE, INTERNAL_SERVER_ERROR;
    }

    private ErrorType code;
    private String message;

    public static ExceptionResponse of(ErrorType code, String message) {
        if (code == null || !StringUtils.hasText(message)) {
            throw new IllegalArgumentException("code & message are mandatory.");
        }
        return new ExceptionResponse(code, message);
    }

}
