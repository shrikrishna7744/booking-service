package com.booking.controller;

import com.booking.model.BookingFilter;
import com.booking.model.BookingModel;
import com.booking.model.UpdateBookingModel;
import com.booking.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingModel> createBooking(@RequestBody @Valid BookingModel bookingDetails) {
        return new ResponseEntity<>(bookingService.createBooking(bookingDetails), HttpStatus.OK);

    }

    @GetMapping
    public ResponseEntity<List<BookingModel>> getAll(BookingFilter filter) {
        return new ResponseEntity<>(bookingService.getAll(filter), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingModel> updateSeat(@PathVariable("id") Long bookingId, @RequestBody @Valid UpdateBookingModel updateBookingModel) {
        return new ResponseEntity<>(bookingService.updateSeat(bookingId, updateBookingModel), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteBooking(@PathVariable("id") Long bookingId) {
        bookingService.deleteBooking(bookingId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
