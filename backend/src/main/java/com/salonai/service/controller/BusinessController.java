package com.salonai.service.controller;

import com.salonai.service.entity.SalonService;
import com.salonai.service.service.BusinessService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/services")
@CrossOrigin("*")
public class BusinessController {

    private final BusinessService businessService;

    public BusinessController(
            BusinessService businessService
    ) {

        this.businessService = businessService;
    }

    // =========================
    // CREATE SERVICE
    // =========================

    @PostMapping
    public Object createService(
            @RequestBody SalonService request
    ) {

        try {

            SalonService savedService =
                    businessService.createService(
                            request
                    );

            return Map.of(
                    "message",
                    "Service created successfully",

                    "service",
                    savedService
            );

        } catch (Exception e) {

            return Map.of(
                    "error",
                    "Service creation failed",

                    "details",
                    e.getMessage()
            );
        }
    }

    // =========================
    // GET ALL SERVICES
    // =========================

    @GetMapping
    public List<SalonService> getAllServices() {

        return businessService.getAllServices();
    }

    // =========================
    // GET SERVICES BY SALON
    // =========================

    @GetMapping("/salon/{salonId}")
    public List<SalonService> getServicesBySalon(
            @PathVariable Long salonId
    ) {

        return businessService.getServicesBySalon(
                salonId
        );
    }

    // =========================
    // DELETE SERVICE
    // =========================

    @DeleteMapping("/{id}")
    public Object deleteService(
            @PathVariable Long id
    ) {

        try {

            businessService.deleteService(
                    id
            );

            return Map.of(
                    "message",
                    "Service deleted successfully"
            );

        } catch (Exception e) {

            return Map.of(
                    "error",
                    "Delete failed",

                    "details",
                    e.getMessage()
            );
        }
    }
}