package com.salonai.booking.controller;

import com.salonai.booking.entity.Booking;
import com.salonai.booking.service.BookingService;
import com.salonai.salon.entity.Salon;
import com.salonai.salon.repository.SalonRepository;

import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    private final SalonRepository salonRepository;

    public BookingController(
            BookingService bookingService,
            SalonRepository salonRepository
    ) {

        this.bookingService = bookingService;

        this.salonRepository = salonRepository;
    }

    @PostMapping
    public Object createBooking(
            @RequestBody Map<String, String> request
    ) {

        try {

            String sessionId =
                    request.get("sessionId");

            String customerName =
                    request.get("customerName");

            String customerPhone =
                    request.get("customerPhone");

            String service =
                    request.get("service");

            String bookingDate =
                    request.get("bookingDate");

            String bookingTime =
                    request.get("bookingTime");

            Long salonId =
                    Long.parseLong(
                            request.get("salonId")
                    );

            Optional<Salon> salonOptional =
                    salonRepository.findById(
                            salonId
                    );

            if (salonOptional.isEmpty()) {

                return Map.of(
                        "error",
                        "Salon not found"
                );
            }

            int availableSlots =
                    bookingService.getAvailableSlots(
                            salonId,
                            bookingDate,
                            bookingTime
                    );

            if (availableSlots <= 0) {

                return Map.of(
                        "error",
                        "No slots available"
                );
            }

            Booking booking =
                    bookingService.createBooking(
                            sessionId,
                            salonId,
                            customerName,
                            customerPhone,
                            service,
                            bookingDate,
                            bookingTime
                    );

            return Map.of(
                    "message",
                    "Booking created successfully",

                    "bookingId",
                    booking.getId(),

                    "salon",
                    booking.getSalon().getSalonName(),

                    "customerName",
                    booking.getCustomerName(),

                    "customerPhone",
                    booking.getCustomerPhone(),

                    "service",
                    booking.getService(),

                    "bookingDate",
                    booking.getBookingDate(),

                    "bookingTime",
                    booking.getBookingTime(),

                    "remainingSlots",
                    availableSlots - 1
            );

        } catch (Exception e) {

            return Map.of(
                    "error",
                    "Booking failed",

                    "details",
                    e.getMessage()
            );
        }
    }

    @GetMapping("/latest/{sessionId}")
    public Object getLatestBooking(
            @PathVariable String sessionId
    ) {

        Optional<Booking> bookingOptional =
                bookingService.getLatestBooking(
                        sessionId
                );

        if (bookingOptional.isEmpty()) {

            return Map.of(
                    "message",
                    "No booking found"
            );
        }

        Booking booking =
                bookingOptional.get();

        return Map.of(
                "bookingId",
                booking.getId(),

                "salon",
                booking.getSalon().getSalonName(),

                "customerName",
                booking.getCustomerName(),

                "customerPhone",
                booking.getCustomerPhone(),

                "service",
                booking.getService(),

                "bookingDate",
                booking.getBookingDate(),

                "bookingTime",
                booking.getBookingTime()
        );
    }

    @GetMapping("/slots")
    public Object getAvailableSlots(
            @RequestParam Long salonId,

            @RequestParam String date,

            @RequestParam String time
    ) {

        int availableSlots =
                bookingService.getAvailableSlots(
                        salonId,
                        date,
                        time
                );

        return Map.of(
                "salonId",
                salonId,

                "date",
                date,

                "time",
                time,

                "availableSlots",
                availableSlots
        );
    }

    @DeleteMapping("/{bookingId}")
    public Object cancelBooking(
            @PathVariable Long bookingId
    ) {

        try {

            bookingService.cancelBooking(
                    bookingId
            );

            return Map.of(
                    "message",
                    "Booking cancelled successfully"
            );

        } catch (Exception e) {

            return Map.of(
                    "error",
                    "Cancellation failed",

                    "details",
                    e.getMessage()
            );
        }
    }
}