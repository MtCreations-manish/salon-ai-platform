package com.salonai.salon.service;

import com.salonai.salon.entity.Salon;
import com.salonai.salon.repository.SalonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SalonService {

    private final SalonRepository salonRepository;

    public SalonService(
            SalonRepository salonRepository
    ) {

        this.salonRepository =
                salonRepository;
    }

    // =====================================
    // CREATE
    // =====================================

    public Salon createSalon(
            Salon salon
    ) {

        return salonRepository.save(salon);
    }

    // =====================================
    // GET ALL
    // =====================================

    public List<Salon> getAllSalons() {

        return salonRepository.findAll();
    }

    // =====================================
    // GET BY ID
    // =====================================

    public Optional<Salon> getSalonById(
            Long id
    ) {

        return salonRepository.findById(id);
    }

    public Salon updateSalon(Long id, Salon request) {
        Salon salon = salonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Salon not found"));

        if (request.getSalonName() != null) salon.setSalonName(request.getSalonName());
        if (request.getName() != null) salon.setSalonName(request.getName());
        if (request.getDescription() != null) salon.setDescription(request.getDescription());
        if (request.getAddress() != null) salon.setAddress(request.getAddress());
        if (request.getCity() != null) salon.setCity(request.getCity());
        if (request.getArea() != null) salon.setArea(request.getArea());
        if (request.getPhone() != null) salon.setPhone(request.getPhone());
        if (request.getOpeningTime() != null) salon.setOpeningTime(request.getOpeningTime());
        if (request.getClosingTime() != null) salon.setClosingTime(request.getClosingTime());
        if (request.getMaxBookingCapacity() != null) salon.setMaxBookingCapacity(request.getMaxBookingCapacity());
        if (request.getImageUrl() != null) salon.setImageUrl(request.getImageUrl());

        return salonRepository.save(salon);
    }

    // =====================================
    // DELETE
    // =====================================

    public void deleteSalon(
            Long id
    ) {

        salonRepository.deleteById(id);
    }

    // =====================================
    // FIND BY CITY
    // =====================================

    public List<Salon> findByCity(
            String city
    ) {

        return salonRepository
                .findByCityIgnoreCase(city);
    }
}
