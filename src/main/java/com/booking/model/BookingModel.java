package com.booking.model;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingModel {

    @Schema(hidden = true)
    private Long id;
    @NotNull(message = "User details must not be null")
    @Valid
    private User user;
    @NotEmpty(message = "'From' can not be null or empty")
    private String from;
    @NotEmpty(message = "'To' can not be null or empty")
    private String to;
    @Schema(hidden = true)
    private String seatAllocated;
    @Schema(hidden = true)
    private String sectionAllocated;
    @NotNull(message = "'amountPaid' can not be null or empty")
    BigDecimal amountPaid;
    Currency currency = Currency.USD;

}
