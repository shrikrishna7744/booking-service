package com.booking.service.impl;

import com.booking.entity.SeatDetail;
import com.booking.exception.EntityNotFound;
import com.booking.exception.NoSeatAvailableException;
import com.booking.repository.SeatDetailRepository;
import com.booking.service.SeatAllocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
@Slf4j
public class SeatAllocationServiceImpl implements SeatAllocationService {
    @Autowired
    SeatDetailRepository seatDetailRepository;
    Lock lock = new ReentrantLock();

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public SeatDetail allocateSeat() {
        try {
            lock.lock();
            Optional<SeatDetail> any = seatDetailRepository.findAll().stream().filter(x -> x.isAvailable()).findAny();
            SeatDetail seatDetail = null;
            if (!any.isEmpty()) {
                seatDetail = any.get();
                seatDetail.setAvailable(false);
                seatDetailRepository.save(seatDetail);
            } else {
                log.error("No seat available for booking");
                throw new NoSeatAvailableException("No seat available for booking");
            }
            return seatDetail;
        } finally {
            lock.unlock();
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void removeBooing(String seatAllocated) {
        Optional<SeatDetail> first = seatDetailRepository.findAll().stream().filter(x -> x.getSeatId().equalsIgnoreCase(seatAllocated)).findFirst();
        if (!first.isEmpty()) {
            SeatDetail seatDetail = first.get();
            seatDetail.setAvailable(true);
            seatDetailRepository.save(seatDetail);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public SeatDetail getSeat(String seatId) {
        Optional<SeatDetail> opSeat = seatDetailRepository.findById(seatId);
        if (opSeat.isEmpty()) {
            log.error("Seat not found for seatId : {}", seatId);
            throw new EntityNotFound("Seat not found for seatId:" + seatId);
        }
        return opSeat.get();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public SeatDetail assignSeat(String seatNumber) {
        try {
            lock.lock();
            SeatDetail seatDetail = getSeat(seatNumber);
            if (!seatDetail.isAvailable()) {
                log.error("Requested seat is not available seatNumber : {}", seatNumber);
                throw new EntityNotFound("Requested seat is not available");
            }
            seatDetail.setAvailable(false);
            seatDetailRepository.save(seatDetail);
            return seatDetail;
        } finally {
            lock.unlock();
        }
    }
}
