package com.booking.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    enum ErrorType {
        BOOKING_NOT_FOUND, NO_SEAT_AVAILABLE, INTERNAL_SERVER_ERROR;
    }

    @ExceptionHandler(value = EntityNotFound.class)
    public ResponseEntity<ExceptionResponse> handleBookingNotFound(EntityNotFound ex) {
        ExceptionResponse errorResponse = new ExceptionResponse(ErrorType.BOOKING_NOT_FOUND.name(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(value = NoSeatAvailableException.class)
    public ResponseEntity<ExceptionResponse> handleNoBookingAvailable(NoSeatAvailableException ex) {
        ExceptionResponse errorResponse = new ExceptionResponse(ErrorType.NO_SEAT_AVAILABLE.name(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ExceptionResponse> globalException(Exception ex) {
        ExceptionResponse errorResponse = new ExceptionResponse(ErrorType.INTERNAL_SERVER_ERROR.name(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }
}