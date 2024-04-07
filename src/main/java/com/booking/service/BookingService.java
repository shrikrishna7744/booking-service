package com.booking.service;

import java.util.List;

import com.booking.model.BookingFilter;
import com.booking.model.BookingModel;
import com.booking.model.UpdateBookingModel;

public interface BookingService {

    BookingModel createBooking(BookingModel bookingModel);

    List<BookingModel> getAllBookings(BookingFilter filter);

    void deleteBooking(Long bookingId);

    BookingModel updateSeat(Long bookingId, UpdateBookingModel updateBookingModel);

}
