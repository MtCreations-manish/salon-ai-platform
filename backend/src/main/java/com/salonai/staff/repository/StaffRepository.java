package com.salonai.staff.repository;

import com.salonai.staff.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffRepository
        extends JpaRepository<Staff, Long> {
}