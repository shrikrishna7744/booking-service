package com.booking.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UpdateBookingModel {
    @NotEmpty(message = "'seatNumber' can not be null or empty")
    private String seatNumber;
}
