package com.salonai.booking.repository;

import com.salonai.booking.entity.Slot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface SlotRepository extends JpaRepository<Slot, Long> {

    Optional<Slot> findBySalonIdAndSlotDateAndSlotTime(Long salonId, LocalDate slotDate, String slotTime);
}
