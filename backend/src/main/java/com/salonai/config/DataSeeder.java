package com.salonai.config;

import com.salonai.salon.entity.Salon;
import com.salonai.salon.repository.SalonRepository;
import com.salonai.service.entity.SalonService;
import com.salonai.service.repository.SalonServiceRepository;
import com.salonai.staff.entity.Staff;
import com.salonai.staff.repository.StaffRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final SalonRepository salonRepository;
    private final SalonServiceRepository serviceRepository;
    private final StaffRepository staffRepository;

    public DataSeeder(
            SalonRepository salonRepository,
            SalonServiceRepository serviceRepository,
            StaffRepository staffRepository
    ) {
        this.salonRepository = salonRepository;
        this.serviceRepository = serviceRepository;
        this.staffRepository = staffRepository;
    }

    @Override
    public void run(String... args) {
        if (salonRepository.count() > 0) {
            return;
        }

        Salon salon = new Salon();
        salon.setSalonName("Luxe Studio Salon");
        salon.setDescription("Premium hair, spa, and grooming appointments with AI-assisted booking.");
        salon.setAddress("Main Market Road");
        salon.setCity("Bhimtal");
        salon.setArea("Mallital");
        salon.setPhone("9876543210");
        salon.setOpeningTime("09:00");
        salon.setClosingTime("21:00");
        salon.setMaxBookingCapacity(4);
        salon.setImageUrl("https://images.unsplash.com/photo-1560066984-138dadb4c035?auto=format&fit=crop&w=1200&q=80");
        Salon savedSalon = salonRepository.save(salon);

        createService(savedSalon, "Haircut", 499.0, 30);
        createService(savedSalon, "Hair Spa", 1499.0, 60);
        createService(savedSalon, "Facial", 1299.0, 45);

        createStaff(savedSalon, "Aarav", "Stylist", "Haircut", "09:00", "18:00");
        createStaff(savedSalon, "Meera", "Skin Expert", "Facial", "11:00", "20:00");
    }

    private void createService(Salon salon, String name, Double price, Integer duration) {
        SalonService service = new SalonService();
        service.setSalon(salon);
        service.setName(name);
        service.setPrice(price);
        service.setDuration(duration);
        serviceRepository.save(service);
    }

    private void createStaff(Salon salon, String name, String role, String specialization, String start, String end) {
        Staff staff = new Staff();
        staff.setSalon(salon);
        staff.setName(name);
        staff.setRole(role);
        staff.setSpecialization(specialization);
        staff.setPhone("9876500000");
        staff.setStartTime(start);
        staff.setEndTime(end);
        staff.setAvailable(true);
        staff.setAttendanceStatus("PRESENT");
        staffRepository.save(staff);
    }
}
