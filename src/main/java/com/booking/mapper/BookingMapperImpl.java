package com.booking.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.booking.entity.Booking;
import com.booking.model.BookingModel;
import com.booking.model.Currency;
import com.booking.model.User;

@Component
public class BookingMapperImpl implements BookingMapper {

    public Booking mapToEntity(BookingModel bookingModel) {
        Booking booking = new Booking();
        booking.setCurrency(bookingModel.getCurrency().name());
        booking.setAmountPaid(bookingModel.getAmountPaid());
        booking.setFirstName(bookingModel.getUser().getFirstName());
        booking.setLastName(bookingModel.getUser().getLastName());
        booking.setEmail(bookingModel.getUser().getEmail());
        booking.setFromLocation(bookingModel.getFrom());
        booking.setToLocation(bookingModel.getTo());
        booking.setSectionAllocated(bookingModel.getSectionAllocated());
        booking.setSeatAllocated(bookingModel.getSeatAllocated());
        return booking;
    }

    public BookingModel mapToModel(Booking booking) {
        BookingModel model = new BookingModel();
        model.setAmountPaid(booking.getAmountPaid());
        model.setCurrency(Currency.valueOf(booking.getCurrency()));
        model.setTo(booking.getToLocation());
        model.setFrom(booking.getFromLocation());
        model.setSectionAllocated(booking.getSectionAllocated());
        model.setSeatAllocated(booking.getSeatAllocated());
        model.setId(booking.getId());
        model.setUser(createUser(booking));
        return model;
    }

    private User createUser(Booking booking) {
        User user = new User();
        user.setFirstName(booking.getFirstName());
        user.setLastName(booking.getLastName());
        user.setEmail(booking.getEmail());
        return user;
    }

    public List<Booking> mapToEntity(List<BookingModel> bookingModel) {
        return bookingModel.stream().map(bm -> mapToEntity(bm)).toList();
    }

    public List<BookingModel> mapToModel(List<Booking> booking) {
        return booking.stream().map(bm -> mapToModel(bm)).toList();
    }

}
