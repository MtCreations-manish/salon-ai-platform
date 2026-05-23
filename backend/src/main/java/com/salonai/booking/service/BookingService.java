package com.salonai.booking.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salonai.booking.entity.Booking;
import com.salonai.booking.repository.BookingRepository;
import com.salonai.salon.entity.Salon;
import com.salonai.salon.repository.SalonRepository;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private SalonRepository salonRepository;

    // =========================================
    // CREATE BOOKING
    // =========================================

    public Booking createBooking(
            String sessionId,
            Long salonId,
            String customerName,
            String customerPhone,
            String service,
            String bookingDate,
            String bookingTime
    ) {

        Salon salon = salonRepository.findById(salonId)
                .orElseThrow(() ->
                        new RuntimeException("Salon not found"));

        Booking booking = new Booking();

        booking.setSalon(salon);

        booking.setCustomerName(customerName);

        booking.setCustomerPhone(customerPhone);

        booking.setService(service);

        booking.setBookingDate(
                LocalDate.parse(bookingDate)
        );

        booking.setBookingTime(bookingTime);

        booking.setStatus("CONFIRMED");

        return bookingRepository.save(booking);
    }

    // =========================================
    // AVAILABLE SLOTS
    // =========================================

    public int getAvailableSlots(
            Long salonId,
            String bookingDate
    ) {

        LocalDate date =
                LocalDate.parse(bookingDate);

        List<Booking> bookings =
                bookingRepository.findBySalonIdAndBookingDate(
                        salonId,
                        date
                );

        int totalSlots = 10;

        int bookedSlots = bookings.size();

        return totalSlots - bookedSlots;
    }

    // =========================================
    // GET AVAILABLE SLOTS BY TIME
    // =========================================

    public int getAvailableSlots(
            Long salonId,
            String bookingDate,
            String bookingTime
    ) {

        LocalDate date =
                LocalDate.parse(bookingDate);

        long bookedSlots =
                bookingRepository
                        .countBySalonIdAndBookingDateAndBookingTime(
                                salonId,
                                date,
                                bookingTime
                        );

        int maxSlotsPerTime = 5;

        return maxSlotsPerTime - (int) bookedSlots;
    }

    // =========================================
    // GET ALL BOOKINGS
    // =========================================

    public List<Booking> getAllBookings() {

        return bookingRepository.findAll();
    }

    // =========================================
    // GET LATEST BOOKING
    // =========================================

    public Optional<Booking> getLatestBooking(
            String sessionId
    ) {

        List<Booking> bookings =
                bookingRepository.findAll();

        if (bookings.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(
                bookings.get(bookings.size() - 1)
        );
    }

    // =========================================
    // CANCEL BOOKING
    // =========================================

    public void cancelBooking(Long bookingId) {

        Booking booking =
                bookingRepository.findById(bookingId)
                        .orElseThrow(() ->
                                new RuntimeException("Booking not found"));

        booking.setStatus("CANCELLED");

        bookingRepository.save(booking);
    }
}