package com.salonai.salon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.salonai.salon.entity.Salon;

@Repository
public interface SalonRepository extends JpaRepository<Salon, Long> {

    List<Salon> findByCityIgnoreCase(String city);

}