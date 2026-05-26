package com.salonai.staff.service;

import com.salonai.salon.entity.Salon;
import com.salonai.salon.repository.SalonRepository;
import com.salonai.staff.dto.CreateStaffRequest;
import com.salonai.staff.entity.Staff;
import com.salonai.staff.entity.StaffAttendance;
import com.salonai.staff.repository.StaffAttendanceRepository;
import com.salonai.staff.repository.StaffRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StaffService {

    private final StaffRepository staffRepository;
    private final SalonRepository salonRepository;
    private final StaffAttendanceRepository attendanceRepository;

    public StaffService(
            StaffRepository staffRepository,
            SalonRepository salonRepository,
            StaffAttendanceRepository attendanceRepository
    ) {
        this.staffRepository = staffRepository;
        this.salonRepository = salonRepository;
        this.attendanceRepository = attendanceRepository;
    }

    public Staff createStaff(
            CreateStaffRequest request
    ) {

        Salon salon = salonRepository.findById(
                request.getSalonId()
        ).orElseThrow();

        Staff staff = new Staff();

        staff.setName(request.getName());
        staff.setRole(request.getRole());
        staff.setPhone(request.getPhone());
        staff.setSpecialization(request.getSpecialization());
        staff.setAvailable(request.getAvailable() == null || request.getAvailable());
        staff.setAttendanceStatus(request.getAttendanceStatus() == null ? "PRESENT" : request.getAttendanceStatus());
        staff.setStartTime(request.getStartTime());
        staff.setEndTime(request.getEndTime());
        staff.setSalon(salon);

        return staffRepository.save(staff);
    }

    public List<Staff> getAllStaff() {

        return staffRepository.findAll();
    }

    public List<Staff> getStaffBySalon(Long salonId) {
        return staffRepository.findBySalonId(salonId);
    }

    public Staff updateStaff(Long id, CreateStaffRequest request) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        if (request.getName() != null) staff.setName(request.getName());
        if (request.getRole() != null) staff.setRole(request.getRole());
        if (request.getPhone() != null) staff.setPhone(request.getPhone());
        if (request.getSpecialization() != null) staff.setSpecialization(request.getSpecialization());
        if (request.getAvailable() != null) staff.setAvailable(request.getAvailable());
        if (request.getAttendanceStatus() != null) staff.setAttendanceStatus(request.getAttendanceStatus());
        if (request.getStartTime() != null) staff.setStartTime(request.getStartTime());
        if (request.getEndTime() != null) staff.setEndTime(request.getEndTime());

        return staffRepository.save(staff);
    }

    public Staff setAvailability(Long id, Boolean available) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        staff.setAvailable(available);
        return staffRepository.save(staff);
    }

    public StaffAttendance markAttendance(Long staffId, String status, LocalDate date) {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        StaffAttendance attendance = attendanceRepository
                .findByStaffIdAndAttendanceDate(staffId, date)
                .orElseGet(StaffAttendance::new);

        attendance.setStaff(staff);
        attendance.setSalon(staff.getSalon());
        attendance.setAttendanceDate(date);
        attendance.setStatus(status);

        staff.setAttendanceStatus(status);
        staff.setAvailable("PRESENT".equalsIgnoreCase(status));
        staffRepository.save(staff);

        return attendanceRepository.save(attendance);
    }

    public Map<String, Object> attendanceSummary(Long salonId, LocalDate date) {
        List<Staff> staff = staffRepository.findBySalonId(salonId);
        long present = staff.stream().filter(s -> "PRESENT".equalsIgnoreCase(s.getAttendanceStatus())).count();
        long absent = staff.stream().filter(s -> "ABSENT".equalsIgnoreCase(s.getAttendanceStatus())).count();
        long leave = staff.stream().filter(s -> "LEAVE".equalsIgnoreCase(s.getAttendanceStatus())).count();
        long available = staff.stream().filter(s -> Boolean.TRUE.equals(s.getAvailable())).count();

        Map<String, Object> summary = new HashMap<>();
        summary.put("date", date);
        summary.put("present", present);
        summary.put("absent", absent);
        summary.put("leave", leave);
        summary.put("workingToday", present);
        summary.put("totalAvailableStaff", available);
        summary.put("staff", staff);
        summary.put("records", attendanceRepository.findBySalonIdAndAttendanceDate(salonId, date));
        return summary;
    }

    public void deleteStaff(Long id) {
        staffRepository.deleteById(id);
    }
}
