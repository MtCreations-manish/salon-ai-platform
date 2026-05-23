package com.salonai.salon.controller;

import com.salonai.salon.entity.Salon;
import com.salonai.salon.service.SalonService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/salons")
@CrossOrigin("*")
public class SalonController {

    private final SalonService salonService;

    public SalonController(
            SalonService salonService
    ) {

        this.salonService = salonService;
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

            return salonOptional.get();
        }

        return Map.of(
                "error",
                "Salon not found"
        );
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