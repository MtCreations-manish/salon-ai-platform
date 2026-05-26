package com.salonai.staff.controller;

import com.salonai.staff.dto.CreateStaffRequest;
import com.salonai.staff.entity.Staff;
import com.salonai.staff.service.StaffService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/staff")
@CrossOrigin("*")
public class StaffController {

    private final StaffService staffService;

    public StaffController(
            StaffService staffService
    ) {
        this.staffService = staffService;
    }

    @PostMapping
    public Staff createStaff(
            @RequestBody CreateStaffRequest request
    ) {

        return staffService.createStaff(request);
    }

    @GetMapping
    public List<Staff> getAllStaff() {

        return staffService.getAllStaff();
    }

    @GetMapping("/salon/{salonId}")
    public List<Staff> getStaffBySalon(@PathVariable Long salonId) {
        return staffService.getStaffBySalon(salonId);
    }

    @PutMapping("/{id}")
    public Staff updateStaff(@PathVariable Long id, @RequestBody CreateStaffRequest request) {
        return staffService.updateStaff(id, request);
    }

    @PatchMapping("/{id}/availability")
    public Staff updateAvailability(@PathVariable Long id, @RequestBody Map<String, Boolean> request) {
        return staffService.setAvailability(id, request.getOrDefault("available", true));
    }

    @PostMapping("/{id}/attendance")
    public Object markAttendance(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String status = request.getOrDefault("status", "PRESENT");
        LocalDate date = LocalDate.parse(request.getOrDefault("date", LocalDate.now().toString()));
        return staffService.markAttendance(id, status, date);
    }

    @GetMapping("/attendance")
    public Map<String, Object> attendanceSummary(@RequestParam Long salonId, @RequestParam(required = false) String date) {
        LocalDate effectiveDate = date == null ? LocalDate.now() : LocalDate.parse(date);
        return staffService.attendanceSummary(salonId, effectiveDate);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> deleteStaff(@PathVariable Long id) {
        staffService.deleteStaff(id);
        return Map.of("message", "Staff deleted successfully");
    }
}
