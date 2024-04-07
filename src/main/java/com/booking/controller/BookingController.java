package com.booking.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.booking.model.BookingFilter;
import com.booking.model.BookingModel;
import com.booking.model.UpdateBookingModel;
import com.booking.service.BookingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/booking")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(final BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingModel> createBooking(@RequestBody @Valid BookingModel bookingDetails) {
        return ResponseEntity.ok(bookingService.createBooking(bookingDetails));

    }

    @GetMapping
    public ResponseEntity<List<BookingModel>> getAll(BookingFilter filter) {
        return ResponseEntity.ok(bookingService.getAllBookings(filter));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingModel> updateSeat(@PathVariable("id") Long bookingId,
                                                   @RequestBody @Valid UpdateBookingModel updateBookingModel) {
        return ResponseEntity.ok(bookingService.updateSeat(bookingId, updateBookingModel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable("id") Long bookingId) {
        bookingService.deleteBooking(bookingId);
        return ResponseEntity.ok().build();
    }

}
