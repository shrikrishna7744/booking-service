package com.booking.service.impl;

import static com.booking.service.impl.BookingTestUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.booking.entity.Booking;
import com.booking.entity.SeatDetail;
import com.booking.exception.EntityNotFound;
import com.booking.mapper.BookingMapper;
import com.booking.model.BookingFilter;
import com.booking.model.BookingModel;
import com.booking.model.UpdateBookingModel;
import com.booking.repository.BookingRepository;
import com.booking.service.SeatAllocationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {BookingServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class BookingServiceImplTest {
    @MockBean
    private BookingMapper bookingMapper;

    @MockBean
    private BookingRepository bookingRepository;

    @Autowired
    private BookingServiceImpl bookingServiceImpl;

    @MockBean
    private SeatAllocationService seatAllocationService;

    @Test
    void testCreateBooking() {
        // Arrange
        Booking booking = buildBooking();
        when(bookingRepository.save(Mockito.<Booking>any())).thenReturn(booking);

        BookingModel bookingModel = buildBookingModel();

        when(bookingMapper.mapToEntity(Mockito.<BookingModel>any())).thenReturn(booking);
        when(bookingMapper.mapToModel(Mockito.<Booking>any())).thenReturn(bookingModel);

        SeatDetail seatDetail = buildSeatDetails();
        when(seatAllocationService.allocateSeat()).thenReturn(seatDetail);

        // Act
        BookingModel actualCreateBookingResult = bookingServiceImpl.createBooking(bookingModel);

        // Assert
        verify(bookingMapper).mapToEntity(isA(BookingModel.class));
        verify(bookingMapper).mapToModel(isA(Booking.class));
        verify(seatAllocationService).allocateSeat();
        verify(bookingRepository).save(isA(Booking.class));
        assertEquals("1", actualCreateBookingResult.getSeatAllocated());
        assertEquals("1", actualCreateBookingResult.getSectionAllocated());
    }

    @Test
    void testCreateBooking2() {
        // Arrange
        when(bookingMapper.mapToEntity(Mockito.<BookingModel>any()))
                .thenThrow(new EntityNotFound("booking not found"));

        SeatDetail seatDetail = new SeatDetail();
        seatDetail.setAvailable(true);
        seatDetail.setSeatId("42");
        seatDetail.setSectionId("42");
        when(seatAllocationService.allocateSeat()).thenReturn(seatDetail);

        BookingModel model = buildBookingModel();

        // Act and Assert
        assertThrows(EntityNotFound.class, () -> bookingServiceImpl.createBooking(model));
        verify(bookingMapper).mapToEntity(isA(BookingModel.class));
        verify(seatAllocationService).allocateSeat();
    }

    @Test
    void testGetAll() {
        // Arrange
        when(bookingRepository.findAll(Mockito.<Specification<Booking>>any())).thenReturn(new ArrayList<>());
        ArrayList<BookingModel> bookingModelList = new ArrayList<>();
        when(bookingMapper.mapToModel(Mockito.<List<Booking>>any())).thenReturn(bookingModelList);

        BookingFilter filter = new BookingFilter();
        filter.setEmail("krishna@gmail.com");
        filter.setSectionId("1");

        // Act
        List<BookingModel> actualAll = bookingServiceImpl.getAll(filter);

        // Assert
        verify(bookingMapper).mapToModel(isA(List.class));
        verify(bookingRepository).findAll(isA(Specification.class));
        assertTrue(actualAll.isEmpty());
        assertSame(bookingModelList, actualAll);
    }

    @Test
    void testDeleteBooking2() {
        // Arrange
        Booking booking = buildBooking();
        Optional<Booking> ofResult = Optional.of(booking);
        when(bookingRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        doThrow(new EntityNotFound("Booking Entity not found")).when(seatAllocationService)
                .removeBooing(Mockito.<String>any());

        // Act and Assert
        assertThrows(EntityNotFound.class, () -> bookingServiceImpl.deleteBooking(1L));
        verify(seatAllocationService).removeBooing(eq("1"));
        verify(bookingRepository).findById(isA(Long.class));
    }

    @Test
    void testDeleteBooking3() {
        // Arrange
        Optional<Booking> emptyResult = Optional.empty();
        when(bookingRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(EntityNotFound.class, () -> bookingServiceImpl.deleteBooking(1L));
        verify(bookingRepository).findById(isA(Long.class));
    }

    @Test
    void testUpdateSeat() {
        // Arrange
        Booking booking = buildBooking();
        Optional<Booking> ofResult = Optional.of(booking);

        Booking booking2 = buildBooking();

        when(bookingRepository.save(Mockito.<Booking>any())).thenReturn(booking2);
        when(bookingRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        BookingModel bookingModel = buildBookingModel();
        when(bookingMapper.mapToModel(Mockito.<Booking>any())).thenReturn(bookingModel);

        SeatDetail seatDetail = buildSeatDetails();
        when(seatAllocationService.assignSeat(Mockito.<String>any())).thenReturn(seatDetail);
        doNothing().when(seatAllocationService).removeBooing(Mockito.<String>any());

        UpdateBookingModel updateBookingModel = new UpdateBookingModel();
        updateBookingModel.setSeatNumber("1");

        // Act
        BookingModel actualUpdateSeatResult = bookingServiceImpl.updateSeat(1L, updateBookingModel);

        // Assert
        verify(bookingMapper).mapToModel(isA(Booking.class));
        verify(seatAllocationService).assignSeat(eq("1"));
        verify(seatAllocationService).removeBooing(eq("1"));
        verify(bookingRepository).findById(isA(Long.class));
        verify(bookingRepository).save(isA(Booking.class));
    }

}
