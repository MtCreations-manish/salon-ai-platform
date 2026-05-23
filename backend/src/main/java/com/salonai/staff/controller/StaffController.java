package com.salonai.staff.controller;

import com.salonai.staff.dto.CreateStaffRequest;
import com.salonai.staff.entity.Staff;
import com.salonai.staff.service.StaffService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
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
}