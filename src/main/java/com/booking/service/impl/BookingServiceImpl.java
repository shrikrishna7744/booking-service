package com.booking.service.impl;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.booking.entity.Booking;
import com.booking.entity.SeatDetail;
import com.booking.exception.EntityNotFound;
import com.booking.mapper.BookingMapper;
import com.booking.model.BookingFilter;
import com.booking.model.BookingModel;
import com.booking.model.UpdateBookingModel;
import com.booking.repository.BookingRepository;
import com.booking.service.BookingService;
import com.booking.service.SeatAllocationService;
import com.booking.specification.UserSpecification;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final SeatAllocationService seatAllocationService;
    private final BookingMapper bookingMapper;

    public BookingServiceImpl(final BookingRepository bookingRepository,
                              final SeatAllocationService seatAllocationService, final BookingMapper bookingMapper) {
        super();
        this.bookingRepository = bookingRepository;
        this.seatAllocationService = seatAllocationService;
        this.bookingMapper = bookingMapper;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public BookingModel createBooking(BookingModel model) {
        log.debug("Inside BookingServiceImpl#createBooking with model: {}", model);
        SeatDetail allocated = seatAllocationService.allocateRandomSeat();
        model.setSeatAllocated(allocated.getSeatId());
        model.setSectionAllocated(allocated.getSectionId());
        return bookingMapper.mapToModel(bookingRepository.save(bookingMapper.mapToEntity(model)));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public List<BookingModel> getAllBookings(BookingFilter filter) {
        log.debug("Inside BookingServiceImpl#getAllBookings with filter: {}", filter);
        Specification<Booking> spec = UserSpecification.filterBy(filter);
        return bookingMapper.mapToModel(bookingRepository.findAll(spec));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteBooking(Long bookingId) {
        log.debug("Inside BookingServiceImpl#deleteBooking with bookingId: {}", bookingId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFound("Booking not found for id:" + bookingId));
        seatAllocationService.deallocateSeat(booking.getSeatAllocated());
        bookingRepository.delete(booking);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public BookingModel updateSeat(Long bookingId, UpdateBookingModel updateBookingModel) {
        log.debug("Inside BookingServiceImpl#updateSeat with bookingId: {}, updateReq: {}", bookingId,
                updateBookingModel);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFound("Booking not found for id:" + bookingId));
        log.debug("booking found: {}", booking);
        seatAllocationService.deallocateSeat(booking.getSeatAllocated());
        SeatDetail newSeat = seatAllocationService.allocateSeat(updateBookingModel.getSeatNumber());
        booking.setSeatAllocated(newSeat.getSeatId());
        booking.setSectionAllocated(newSeat.getSectionId());
        bookingRepository.save(booking);
        return bookingMapper.mapToModel(booking);
    }

}
