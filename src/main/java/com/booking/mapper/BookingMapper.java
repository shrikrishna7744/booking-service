package com.booking.mapper;

import java.util.List;

import com.booking.entity.Booking;
import com.booking.model.BookingModel;

public interface BookingMapper {

    Booking mapToEntity(BookingModel bookingModel);

    BookingModel mapToModel(Booking booking);

    List<Booking> mapToEntity(List<BookingModel> bookingModel);

    List<BookingModel> mapToModel(List<Booking> booking);

}
