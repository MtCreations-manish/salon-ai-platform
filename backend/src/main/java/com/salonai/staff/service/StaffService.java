package com.salonai.staff.service;

import com.salonai.salon.entity.Salon;
import com.salonai.salon.repository.SalonRepository;
import com.salonai.staff.dto.CreateStaffRequest;
import com.salonai.staff.entity.Staff;
import com.salonai.staff.repository.StaffRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffService {

    private final StaffRepository staffRepository;
    private final SalonRepository salonRepository;

    public StaffService(
            StaffRepository staffRepository,
            SalonRepository salonRepository
    ) {
        this.staffRepository = staffRepository;
        this.salonRepository = salonRepository;
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
        staff.setStartTime(request.getStartTime());
        staff.setEndTime(request.getEndTime());
        staff.setSalon(salon);

        return staffRepository.save(staff);
    }

    public List<Staff> getAllStaff() {

        return staffRepository.findAll();
    }
}