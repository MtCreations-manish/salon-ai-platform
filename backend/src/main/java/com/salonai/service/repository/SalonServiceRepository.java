package com.salonai.service.repository;

import com.salonai.service.entity.SalonService;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SalonServiceRepository
        extends JpaRepository<SalonService, Long> {

	List<SalonService> findBySalonId(Long salonId);
}