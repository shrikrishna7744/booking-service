package com.booking.service.impl;

import static com.booking.service.impl.BookingTestUtil.buildSeatDetails;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.booking.entity.SeatDetail;
import com.booking.exception.EntityNotFound;
import com.booking.repository.SeatDetailRepository;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    void testAllocateRandomSeat() {
        // Arrange
        when(seatDetailRepository.findRandomAvailableSeat(Mockito.<Pageable>any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(seatDetailRepository.countAvailableSeats()).thenReturn(3L);

        // Act and Assert
        assertThrows(EntityNotFound.class, () -> seatAllocationServiceImpl.allocateRandomSeat());
        verify(seatDetailRepository).countAvailableSeats();
        verify(seatDetailRepository).findRandomAvailableSeat(isA(Pageable.class));
    }

    @Test
    void testAllocateRandomSeat2() {
        // Arrange
        when(seatDetailRepository.findRandomAvailableSeat(Mockito.<Pageable>any()))
                .thenThrow(new EntityNotFound("Seat details not found"));
        when(seatDetailRepository.countAvailableSeats()).thenReturn(3L);

        // Act and Assert
        assertThrows(EntityNotFound.class, () -> seatAllocationServiceImpl.allocateRandomSeat());
        verify(seatDetailRepository).countAvailableSeats();
        verify(seatDetailRepository).findRandomAvailableSeat(isA(Pageable.class));
    }

    @Test
    void testAllocateSeat2() {
        // Arrange
        SeatDetail seatDetail = buildSeatDetails();

        Optional<SeatDetail> ofResult = Optional.of(seatDetail);
        when(seatDetailRepository.save(Mockito.<SeatDetail>any()))
                .thenThrow(new EntityNotFound("Seat details not found"));
        when(seatDetailRepository.findByIdIfAvailable(Mockito.<String>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(EntityNotFound.class, () -> seatAllocationServiceImpl.allocateSeat("1"));
        verify(seatDetailRepository).findByIdIfAvailable(eq("1"));
        verify(seatDetailRepository).save(isA(SeatDetail.class));
    }

    @Test
    void testDeallocateSeat() {
        // Arrange
        SeatDetail seatDetail = buildSeatDetails();
        Optional<SeatDetail> ofResult = Optional.of(seatDetail);

        SeatDetail seatDetail2 = buildSeatDetails();
        when(seatDetailRepository.save(Mockito.<SeatDetail>any())).thenReturn(seatDetail2);
        when(seatDetailRepository.findById(Mockito.<String>any())).thenReturn(ofResult);

        // Act
        seatAllocationServiceImpl.deallocateSeat("1");

        // Assert
        verify(seatDetailRepository).findById(eq("1"));
        verify(seatDetailRepository).save(isA(SeatDetail.class));
    }

    @Test
    void testDeallocateSeat2() {
        // Arrange
        SeatDetail seatDetail = buildSeatDetails();
        Optional<SeatDetail> ofResult = Optional.of(seatDetail);
        when(seatDetailRepository.save(Mockito.<SeatDetail>any()))
                .thenThrow(new EntityNotFound("Seat details not found"));
        when(seatDetailRepository.findById(Mockito.<String>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(EntityNotFound.class, () -> seatAllocationServiceImpl.deallocateSeat("1"));
        verify(seatDetailRepository).findById(eq("1"));
        verify(seatDetailRepository).save(isA(SeatDetail.class));
    }
}
