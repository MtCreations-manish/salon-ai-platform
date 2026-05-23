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