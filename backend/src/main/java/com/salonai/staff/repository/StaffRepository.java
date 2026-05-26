package com.salonai.staff.repository;

import com.salonai.staff.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StaffRepository
        extends JpaRepository<Staff, Long> {

    List<Staff> findBySalonId(Long salonId);

    List<Staff> findBySalonIdAndAvailableTrueAndAttendanceStatusIgnoreCase(Long salonId, String attendanceStatus);
}
