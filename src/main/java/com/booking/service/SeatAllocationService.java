package com.booking.service;

import com.booking.entity.SeatDetail;

public interface SeatAllocationService {

    SeatDetail allocateRandomSeat();

    SeatDetail allocateSeat(String seatId);

    void deallocateSeat(String seatAllocated);

}
