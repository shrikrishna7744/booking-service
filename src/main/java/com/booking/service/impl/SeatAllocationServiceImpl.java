package com.booking.service.impl;

import java.util.List;
import java.util.Random;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.booking.entity.SeatDetail;
import com.booking.exception.EntityNotFound;
import com.booking.repository.SeatDetailRepository;
import com.booking.service.SeatAllocationService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SeatAllocationServiceImpl implements SeatAllocationService {

    private final SeatDetailRepository seatDetailRepository;
    private final Random random;

    public SeatAllocationServiceImpl(final SeatDetailRepository seatDetailRepository) {
        this.seatDetailRepository = seatDetailRepository;
        this.random = new Random();
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public SeatDetail allocateRandomSeat() {
        long availableSeats = seatDetailRepository.countAvailableSeats();
        if (availableSeats == 0) {
            log.error("Seats are not available, booking invalid.");
            throw new EntityNotFound("No seat is not available, booking invalid.");
        }
        List<SeatDetail> seatDetails = seatDetailRepository
                .findRandomAvailableSeat(PageRequest.of(random.nextInt(Math.toIntExact(availableSeats)), 1))
                .getContent();
        if (seatDetails.isEmpty()) {
            log.error("Seats are not available, booking invalid.");
            throw new EntityNotFound("No seat is not available, booking invalid.");
        }
        SeatDetail randomSeat = seatDetails.get(0);
        randomSeat.setAvailable(false);
        seatDetailRepository.save(randomSeat);
        return randomSeat;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public SeatDetail allocateSeat(String seatId) {
        SeatDetail seatDetail = seatDetailRepository.findByIdIfAvailable(seatId).orElseThrow(() -> {
            log.error("Requested seat is not available, seatNumber: {}", seatId);
            throw new EntityNotFound("Requested seat is not available");
        });
        seatDetail.setAvailable(false);
        seatDetailRepository.save(seatDetail);
        return seatDetail;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void deallocateSeat(String seatId) {
        seatDetailRepository.findById(seatId).ifPresent(seatDetail -> {
            seatDetail.setAvailable(true);
            seatDetailRepository.save(seatDetail);
        });
    }
}
