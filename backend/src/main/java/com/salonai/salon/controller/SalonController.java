package com.salonai.salon.controller;

import com.salonai.booking.service.BookingService;
import com.salonai.salon.entity.Salon;
import com.salonai.salon.service.SalonService;
import com.salonai.service.service.BusinessService;
import com.salonai.staff.service.StaffService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/salons")
@CrossOrigin("*")
public class SalonController {

    private final SalonService salonService;
    private final BookingService bookingService;
    private final StaffService staffService;
    private final BusinessService businessService;

    public SalonController(
            SalonService salonService,
            BookingService bookingService,
            StaffService staffService,
            BusinessService businessService
    ) {

        this.salonService = salonService;
        this.bookingService = bookingService;
        this.staffService = staffService;
        this.businessService = businessService;
    }

    // =========================
    // CREATE SALON
    // =========================

    @PostMapping
    public Object createSalon(
            @RequestBody Salon salon
    ) {

        try {

            Salon savedSalon =
                    salonService.createSalon(
                            salon
                    );

            return Map.of(
                    "message",
                    "Salon created successfully",

                    "salon",
                    savedSalon
            );

        } catch (Exception e) {

            return Map.of(
                    "error",
                    "Salon creation failed",

                    "details",
                    e.getMessage()
            );
        }
    }

    // =========================
    // GET ALL SALONS
    // =========================

    @GetMapping
    public List<Salon> getAllSalons() {

        return salonService.getAllSalons();
    }

    @GetMapping("/marketplace")
    public List<Map<String, Object>> getMarketplaceSalons() {
        return salonService.getAllSalons()
                .stream()
                .map(this::toMarketplaceCard)
                .toList();
    }

    // =========================
    // GET SALON BY ID
    // =========================

    @GetMapping("/{id}")
    public Object getSalonById(
            @PathVariable Long id
    ) {

        var salonOptional =
                salonService.getSalonById(id);

        if (salonOptional.isPresent()) {

            Salon salon = salonOptional.get();
            return Map.of(
                    "salon", salon,
                    "services", businessService.getServicesBySalon(salon.getId()),
                    "availability", toMarketplaceCard(salon)
            );
        }

        return Map.of(
                "error",
                "Salon not found"
        );
    }

    @PutMapping("/{id}")
    public Object updateSalon(@PathVariable Long id, @RequestBody Salon request) {
        try {
            return salonService.updateSalon(id, request);
        } catch (Exception e) {
            return Map.of("error", "Salon update failed", "details", e.getMessage());
        }
    }

    private Map<String, Object> toMarketplaceCard(Salon salon) {
        LocalDate today = LocalDate.now();
        int occupied = bookingService.getOccupiedSlots(salon.getId(), today);
        int capacity = salon.getMaxBookingCapacity() == null ? 1 : salon.getMaxBookingCapacity();
        int availableSlots = Math.max(capacity - occupied, 0);
        boolean open = isOpen(salon);

        Map<String, Object> card = new HashMap<>();
        card.put("id", salon.getId());
        card.put("salonName", salon.getSalonName());
        card.put("name", salon.getSalonName());
        card.put("description", salon.getDescription() == null ? "" : salon.getDescription());
        card.put("address", salon.getAddress() == null ? "" : salon.getAddress());
        card.put("city", salon.getCity() == null ? "" : salon.getCity());
        card.put("area", salon.getArea() == null ? "" : salon.getArea());
        card.put("phone", salon.getPhone() == null ? "" : salon.getPhone());
        card.put("imageUrl", salon.getImageUrl() == null ? "" : salon.getImageUrl());
        card.put("open", open);
        card.put("status", open ? "Open" : "Closed");
        card.put("availableSlots", availableSlots);
        card.put("occupiedSlots", occupied);
        card.put("maxBookingCapacity", capacity);
        card.put("estimatedWaitTime", availableSlots > 0 ? "10-15 min" : "30+ min");
        card.put("services", businessService.getServicesBySalon(salon.getId()));
        card.put("availableStaff", staffService.attendanceSummary(salon.getId(), today).get("totalAvailableStaff"));
        return card;
    }

    private boolean isOpen(Salon salon) {
        try {
            if (salon.getOpeningTime() == null || salon.getClosingTime() == null) {
                return true;
            }
            LocalTime now = LocalTime.now();
            LocalTime open = LocalTime.parse(normalizeTime(salon.getOpeningTime()));
            LocalTime close = LocalTime.parse(normalizeTime(salon.getClosingTime()));
            return !now.isBefore(open) && !now.isAfter(close);
        } catch (Exception e) {
            return true;
        }
    }

    private String normalizeTime(String time) {
        if (time.length() == 5) {
            return time;
        }
        if (time.length() == 4) {
            return "0" + time;
        }
        return time;
    }

    // =========================
    // DELETE SALON
    // =========================

    @DeleteMapping("/{id}")
    public Object deleteSalon(
            @PathVariable Long id
    ) {

        try {

            salonService.deleteSalon(
                    id
            );

            return Map.of(
                    "message",
                    "Salon deleted successfully"
            );

        } catch (Exception e) {

            return Map.of(
                    "error",
                    "Salon delete failed",

                    "details",
                    e.getMessage()
            );
        }
    }
}
