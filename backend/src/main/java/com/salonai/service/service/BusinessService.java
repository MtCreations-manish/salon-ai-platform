package com.salonai.service.service;

import com.salonai.salon.entity.Salon;
import com.salonai.salon.repository.SalonRepository;
import com.salonai.service.entity.SalonService;
import com.salonai.service.repository.SalonServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BusinessService {

    private final SalonServiceRepository salonServiceRepository;

    private final SalonRepository salonRepository;

    public BusinessService(
            SalonServiceRepository salonServiceRepository,
            SalonRepository salonRepository
    ) {

        this.salonServiceRepository =
                salonServiceRepository;

        this.salonRepository =
                salonRepository;
    }

    // =========================
    // CREATE SERVICE
    // =========================

    public SalonService createService(
            SalonService request
    ) {

        if (request.getSalon() == null
                || request.getSalon().getId() == null) {

            throw new RuntimeException(
                    "Salon ID is required"
            );
        }

        Optional<Salon> salonOptional =
                salonRepository.findById(
                        request.getSalon().getId()
                );

        if (salonOptional.isEmpty()) {

            throw new RuntimeException(
                    "Salon not found"
            );
        }

        Salon salon =
                salonOptional.get();

        SalonService service =
                new SalonService();

        service.setName(
                request.getName()
        );

        service.setPrice(
                request.getPrice()
        );

        service.setDuration(
                request.getDuration()
        );

        service.setSalon(
                salon
        );

        return salonServiceRepository.save(
                service
        );
    }

    // =========================
    // GET ALL SERVICES
    // =========================

    public List<SalonService> getAllServices() {

        return salonServiceRepository.findAll();
    }

    // =========================
    // GET SERVICES BY SALON
    // =========================

    public List<SalonService> getServicesBySalon(
            Long salonId
    ) {

        return salonServiceRepository
                .findBySalonId(
                        salonId
                );
    }

    // =========================
    // GET SINGLE SERVICE
    // =========================

    public Optional<SalonService> getServiceById(
            Long id
    ) {

        return salonServiceRepository.findById(
                id
        );
    }

    // =========================
    // DELETE SERVICE
    // =========================

    public void deleteService(
            Long id
    ) {

        salonServiceRepository.deleteById(
                id
        );
    }
}