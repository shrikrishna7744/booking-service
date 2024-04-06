package com.booking.service;

import com.booking.model.BookingFilter;
import com.booking.model.BookingModel;
import com.booking.model.UpdateBookingModel;

import java.util.List;

public interface BookingService {
    BookingModel createBooking(BookingModel bookingModel);

    List<BookingModel> getAll(BookingFilter filter);

    void deleteBooking(Long bookingId);

    BookingModel updateSeat(Long bookingId, UpdateBookingModel updateBookingModel);
}
