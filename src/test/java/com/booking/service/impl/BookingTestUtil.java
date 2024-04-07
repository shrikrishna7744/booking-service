package com.booking.service.impl;

import java.math.BigDecimal;

import com.booking.entity.Booking;
import com.booking.entity.SeatDetail;
import com.booking.model.BookingModel;
import com.booking.model.Currency;
import com.booking.model.User;

public class BookingTestUtil {

    public static BookingModel buildBookingModel() {
        User user = new User();
        user.setEmail("krishna@gmail.com");
        user.setFirstName("Krishna");
        user.setLastName("Verma");

        BookingModel bookingModel = new BookingModel();
        bookingModel.setAmountPaid(new BigDecimal("2.3"));
        bookingModel.setCurrency(Currency.INR);
        bookingModel.setFrom("Bangalore");
        bookingModel.setId(1L);
        bookingModel.setSeatAllocated("1");
        bookingModel.setSectionAllocated("1");
        bookingModel.setTo("Mumbai");
        bookingModel.setUser(user);
        return bookingModel;
    }

    public static Booking buildBooking() {
        Booking booking = new Booking();
        booking.setAmountPaid(new BigDecimal("2.3"));
        booking.setCurrency("INR");
        booking.setEmail("krishna@gmail.com");
        booking.setFirstName("Krishna");
        booking.setFromLocation("Bangalore");
        booking.setId(1L);
        booking.setLastName("Verma");
        booking.setSeatAllocated("1");
        booking.setSectionAllocated("1");
        booking.setToLocation("Mumbai");
        return booking;
    }

    public static SeatDetail buildSeatDetails() {
        SeatDetail seatDetail = new SeatDetail();
        seatDetail.setAvailable(true);
        seatDetail.setSeatId("1");
        seatDetail.setSectionId("1");
        return seatDetail;
    }

}
