package com.booking.mapper;

import com.booking.entity.Booking;
import com.booking.model.BookingModel;

import java.util.List;

public interface BookingMapper {
    Booking mapToEntity(BookingModel bookingModel);

    BookingModel mapToModel(Booking booking);

    List<Booking> mapToEntity(List<BookingModel> bookingModel);

    List<BookingModel> mapToModel(List<Booking> booking);

}
