package com.salonai.booking.repository;

import com.salonai.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository
        extends JpaRepository<Booking, Long> {

    List<Booking> findBySalonIdAndBookingDate(
            Long salonId,
            LocalDate bookingDate
    );

    long countBySalonIdAndBookingDateAndBookingTime(
            Long salonId,
            LocalDate bookingDate,
            String bookingTime
    );

//    List<Booking> findBySalonIdAndBookingDate(Long salonId, LocalDate date);
}