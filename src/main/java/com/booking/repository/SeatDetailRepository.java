package com.booking.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.booking.entity.SeatDetail;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface SeatDetailRepository extends JpaRepository<SeatDetail, String> {

    @Query("SELECT COUNT(se) FROM SeatDetail se WHERE se.available = true")
    Long countAvailableSeats();

    @Query("SELECT se FROM SeatDetail se WHERE se.seatId = :seatId AND se.available = true")
    Optional<SeatDetail> findByIdIfAvailable(@Param("seatId") String seatId);

    @Query("SELECT se FROM SeatDetail se WHERE se.available = true")
    Page<SeatDetail> findRandomAvailableSeat(Pageable pageable);

}
