package com.booking.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class User {

    @NotEmpty(message = "First name can not be null or empty")
    private String firstName;
    private String lastName;
    @NotEmpty(message = "Email name can not be null or empty")
    private String email;
}
