package com.booking.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "fromLocation")
    private String fromLocation;

    @Column(name = "toLocation")
    private String toLocation;

    @Column(name = "seat_allocated")
    private String seatAllocated;

    @Column(name = "section_allocated")
    private String sectionAllocated;

    @Column(name = "amount_paid")
    private BigDecimal amountPaid;

    @Column(name = "currency")
    private String currency;
}
