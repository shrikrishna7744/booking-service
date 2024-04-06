package com.booking.repository;

import com.booking.entity.SeatDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface SeatDetailRepository extends JpaRepository<SeatDetail, String> {

    List<SeatDetail> findByIsAvailable(boolean isAvailable);
}
