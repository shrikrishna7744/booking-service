package com.booking.service.impl;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    SeatAllocationService seatAllocationService;

    @Autowired
    BookingMapper mapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public BookingModel createBooking(BookingModel model) {
        SeatDetail allocated = seatAllocationService.allocateSeat();
        model.setSeatAllocated(allocated.getSeatId());
        model.setSectionAllocated(allocated.getSectionId());
        return mapper.mapToModel(bookingRepository.save(mapper.mapToEntity(model)));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public List<BookingModel> getAll(BookingFilter filter) {
        Specification<Booking> spec = UserSpecification.filterBy(filter);
        return mapper.mapToModel(bookingRepository.findAll(spec));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteBooking(Long bookingId) {
        Booking booking = getBooking(bookingId);
        seatAllocationService.removeBooing(booking.getSeatAllocated());
        bookingRepository.delete(booking);
    }

    @Override
    public BookingModel updateSeat(Long bookingId, UpdateBookingModel updateBookingModel) {
        Booking booking = getBooking(bookingId);
        seatAllocationService.removeBooing(booking.getSeatAllocated());
        SeatDetail newSeat = seatAllocationService.assignSeat(updateBookingModel.getSeatNumber());
        booking.setSeatAllocated(newSeat.getSeatId());
        booking.setSectionAllocated(newSeat.getSectionId());
        bookingRepository.save(booking);
        return mapper.mapToModel(booking);
    }

    private Booking getBooking(Long bookingId) {
        Optional<Booking> opBooking = bookingRepository.findById(bookingId);
        if (opBooking.isEmpty()) {
            log.error("Booking not found for id {}", bookingId);
            throw new EntityNotFound("Booking not found for id:" + bookingId);
        }
        return opBooking.get();
    }
}
