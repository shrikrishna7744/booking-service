package com.booking.service;

import com.booking.entity.SeatDetail;


public interface SeatAllocationService {
    SeatDetail allocateSeat();

    void removeBooing(String seatAllocated);

    SeatDetail getSeat(String seatId);

    SeatDetail assignSeat(String seatNumber);
}
