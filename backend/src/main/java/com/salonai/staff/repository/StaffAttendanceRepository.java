package com.salonai.staff.repository;

import com.salonai.staff.entity.StaffAttendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StaffAttendanceRepository extends JpaRepository<StaffAttendance, Long> {

    List<StaffAttendance> findBySalonIdAndAttendanceDate(Long salonId, LocalDate attendanceDate);

    Optional<StaffAttendance> findByStaffIdAndAttendanceDate(Long staffId, LocalDate attendanceDate);
}
