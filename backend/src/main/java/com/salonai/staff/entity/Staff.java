package com.salonai.staff.entity;

import com.salonai.salon.entity.Salon;
import jakarta.persistence.*;

@Entity
@Table(name = "staff")
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String role;

    private String phone;

    private String specialization;

    private Boolean available = true;

    private String attendanceStatus = "PRESENT";

    private String startTime;

    private String endTime;

    @ManyToOne
    @JoinColumn(name = "salon_id")
    private Salon salon;

    public Staff() {
    }

    public Long getId() {
        return id;
    }

    public void setId(
            Long id
    ) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(
            String name
    ) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(
            String role
    ) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(
            String startTime
    ) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(
            String endTime
    ) {
        this.endTime = endTime;
    }

    public Salon getSalon() {
        return salon;
    }

    public void setSalon(
            Salon salon
    ) {
        this.salon = salon;
    }
}
