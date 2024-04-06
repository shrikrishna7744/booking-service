package com.booking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "seat_details")
@AllArgsConstructor
@NoArgsConstructor
public class SeatDetail {

    @Id
    @Column(name = "seat_id")
    private String seatId;

    @Column(name = "section_id")
    private String sectionId;

    @Column(name = "is_available")
    private boolean isAvailable;
}
