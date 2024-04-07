package com.booking.service.impl;

import static com.booking.service.impl.BookingTestUtil.buildSeatDetails;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import com.booking.entity.SeatDetail;
import com.booking.exception.NoSeatAvailableException;
import com.booking.repository.SeatDetailRepository;

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
        assertThrows(NoSeatAvailableException.class, () -> seatAllocationServiceImpl.allocateRandomSeat());
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
        assertThrows(NoSeatAvailableException.class, () -> seatAllocationServiceImpl.allocateRandomSeat());
        verify(seatDetailRepository).save(isA(SeatDetail.class));
        verify(seatDetailRepository).findAll();
    }

    @Test
    void testRemoveBooing() {
        // Arrange
        when(seatDetailRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        seatAllocationServiceImpl.deallocateSeat("Seat Allocated");

        // Assert that nothing has changed
        verify(seatDetailRepository).findAll();
    }

    @Test
    void testRemoveBooing3() {
        // Arrange
        when(seatDetailRepository.findAll()).thenThrow(new NoSeatAvailableException("An error occurred"));

        // Act and Assert
        assertThrows(NoSeatAvailableException.class, () -> seatAllocationServiceImpl.deallocateSeat("Seat Allocated"));
        verify(seatDetailRepository).findAll();
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
        SeatDetail actualAssignSeatResult = seatAllocationServiceImpl.allocateSeat("1");

        // Assert
        verify(seatDetailRepository).findById("1");
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
        assertThrows(NoSeatAvailableException.class, () -> seatAllocationServiceImpl.allocateSeat("1"));
        verify(seatDetailRepository).findById("1");
        verify(seatDetailRepository).save(isA(SeatDetail.class));
    }

}
