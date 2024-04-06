package com.booking.service.impl;

import static com.booking.service.impl.BookingTestUtil.buildSeatDetails;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.booking.entity.SeatDetail;
import com.booking.exception.EntityNotFound;
import com.booking.exception.NoSeatAvailableException;
import com.booking.repository.SeatDetailRepository;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {SeatAllocationServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class SeatAllocationServiceImplTest {
    @Autowired
    private SeatAllocationServiceImpl seatAllocationServiceImpl;

    @MockBean
    private SeatDetailRepository seatDetailRepository;


    @Test
    void testAllocateSeat() {
        // Arrange
        when(seatDetailRepository.findAll()).thenReturn(new ArrayList<>());

        // Act and Assert
        assertThrows(NoSeatAvailableException.class, () -> seatAllocationServiceImpl.allocateSeat());
        verify(seatDetailRepository).findAll();
    }

    @Test
    void testAllocateSeat2() {
        // Arrange
        SeatDetail seatDetail = buildSeatDetails();

        ArrayList<SeatDetail> seatDetailList = new ArrayList<>();
        seatDetailList.add(seatDetail);
        when(seatDetailRepository.save(Mockito.<SeatDetail>any()))
                .thenThrow(new NoSeatAvailableException("An error occurred"));
        when(seatDetailRepository.findAll()).thenReturn(seatDetailList);

        // Act and Assert
        assertThrows(NoSeatAvailableException.class, () -> seatAllocationServiceImpl.allocateSeat());
        verify(seatDetailRepository).save(isA(SeatDetail.class));
        verify(seatDetailRepository).findAll();
    }

    @Test
    void testRemoveBooing() {
        // Arrange
        when(seatDetailRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        seatAllocationServiceImpl.removeBooing("Seat Allocated");

        // Assert that nothing has changed
        verify(seatDetailRepository).findAll();
    }

    @Test
    void testRemoveBooing3() {
        // Arrange
        when(seatDetailRepository.findAll()).thenThrow(new NoSeatAvailableException("An error occurred"));

        // Act and Assert
        assertThrows(NoSeatAvailableException.class, () -> seatAllocationServiceImpl.removeBooing("Seat Allocated"));
        verify(seatDetailRepository).findAll();
    }

    @Test
    void testGetSeat() {
        // Arrange
        SeatDetail seatDetail = buildSeatDetails();
        Optional<SeatDetail> ofResult = Optional.of(seatDetail);
        when(seatDetailRepository.findById(Mockito.<String>any())).thenReturn(ofResult);

        // Act
        SeatDetail actualSeat = seatAllocationServiceImpl.getSeat("1");

        // Assert
        verify(seatDetailRepository).findById(eq("1"));
        assertSame(seatDetail, actualSeat);
    }

    @Test
    void testGetSeat2() {
        // Arrange
        Optional<SeatDetail> emptyResult = Optional.empty();
        when(seatDetailRepository.findById(Mockito.<String>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(EntityNotFound.class, () -> seatAllocationServiceImpl.getSeat("1"));
        verify(seatDetailRepository).findById(eq("1"));
    }

    @Test
    void testGetSeat3() {
        // Arrange
        when(seatDetailRepository.findById(Mockito.<String>any()))
                .thenThrow(new NoSeatAvailableException("No seat available"));

        // Act and Assert
        assertThrows(NoSeatAvailableException.class, () -> seatAllocationServiceImpl.getSeat("1"));
        verify(seatDetailRepository).findById(eq("1"));
    }

    @Test
    void testAssignSeat() {
        // Arrange
        SeatDetail seatDetail = buildSeatDetails();
        Optional<SeatDetail> ofResult = Optional.of(seatDetail);

        SeatDetail seatDetail2 = buildSeatDetails();
        when(seatDetailRepository.save(Mockito.<SeatDetail>any())).thenReturn(seatDetail2);
        when(seatDetailRepository.findById(Mockito.<String>any())).thenReturn(ofResult);

        // Act
        SeatDetail actualAssignSeatResult = seatAllocationServiceImpl.assignSeat("1");

        // Assert
        verify(seatDetailRepository).findById(eq("1"));
        verify(seatDetailRepository).save(isA(SeatDetail.class));
        assertFalse(actualAssignSeatResult.isAvailable());
    }

    @Test
    void testAssignSeat2() {
        // Arrange
        SeatDetail seatDetail = buildSeatDetails();
        Optional<SeatDetail> ofResult = Optional.of(seatDetail);
        when(seatDetailRepository.save(Mockito.<SeatDetail>any()))
                .thenThrow(new NoSeatAvailableException("No seat available"));
        when(seatDetailRepository.findById(Mockito.<String>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(NoSeatAvailableException.class, () -> seatAllocationServiceImpl.assignSeat("1"));
        verify(seatDetailRepository).findById(eq("1"));
        verify(seatDetailRepository).save(isA(SeatDetail.class));
    }

}
