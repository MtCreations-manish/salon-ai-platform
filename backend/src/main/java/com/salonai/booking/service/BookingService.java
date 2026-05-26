package com.salonai.booking.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salonai.booking.entity.Booking;
import com.salonai.booking.repository.BookingRepository;
import com.salonai.notification.service.NotificationService;
import com.salonai.salon.entity.Salon;
import com.salonai.salon.repository.SalonRepository;
import com.salonai.staff.entity.Staff;
import com.salonai.staff.repository.StaffRepository;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private SalonRepository salonRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private NotificationService notificationService;

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
        booking.setSessionId(sessionId);

        booking.setCustomerName(customerName);

        booking.setCustomerPhone(customerPhone);

        booking.setService(service);

        booking.setBookingDate(
                LocalDate.parse(bookingDate)
        );

        booking.setBookingTime(bookingTime);

        booking.setStatus("CONFIRMED");

        Staff assignedStaff = assignStaff(salonId);
        booking.setAssignedStaff(assignedStaff);

        Booking savedBooking = bookingRepository.save(booking);
        notificationService.bookingCreated(savedBooking);
        return savedBooking;
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

        Salon salon = salonRepository.findById(salonId)
                .orElseThrow(() -> new RuntimeException("Salon not found"));

        int totalSlots = capacityFor(salon);

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

        Salon salon = salonRepository.findById(salonId)
                .orElseThrow(() -> new RuntimeException("Salon not found"));

        long bookedSlots =
                bookingRepository
                        .countBySalonIdAndBookingDateAndBookingTimeAndStatusNot(
                                salonId,
                                date,
                                bookingTime,
                                "CANCELLED"
                        );

        int maxSlotsPerTime = capacityFor(salon);

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
                bookingRepository.findBySessionIdOrderByIdDesc(sessionId);

        if (bookings.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(bookings.get(0));
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

    public List<Booking> getBookingsBySalon(Long salonId) {
        return bookingRepository.findBySalonIdOrderByBookingDateDescBookingTimeDesc(salonId);
    }

    public List<Booking> getUpcomingBookings(Long salonId) {
        return bookingRepository.findTop10BySalonIdOrderByBookingDateDescBookingTimeDesc(salonId);
    }

    public int getOccupiedSlots(Long salonId, LocalDate date) {
        return (int) bookingRepository.countBySalonIdAndBookingDateAndStatusNot(
                salonId,
                date,
                "CANCELLED"
        );
    }

    private int capacityFor(Salon salon) {
        if (salon.getMaxBookingCapacity() == null || salon.getMaxBookingCapacity() < 1) {
            return 1;
        }
        return salon.getMaxBookingCapacity();
    }

    private Staff assignStaff(Long salonId) {
        return staffRepository
                .findBySalonIdAndAvailableTrueAndAttendanceStatusIgnoreCase(salonId, "PRESENT")
                .stream()
                .findFirst()
                .orElse(null);
    }
}
