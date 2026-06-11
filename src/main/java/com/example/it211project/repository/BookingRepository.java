package com.example.it211project.repository;

import com.example.it211project.model.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    boolean existsByCourtIdAndBookingDateAndTimeSlot(
            Long courtId,
            LocalDate bookingDate,
            String timeSlot
    );

    List<Booking> findByCustomerId(Long customerId);
}
