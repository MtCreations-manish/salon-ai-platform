package com.salonai.staff.dto;

public class CreateStaffRequest {

    private String name;

    private String role;

    private String phone;

    private String specialization;

    private Boolean available;

    private String attendanceStatus;

    private String startTime;

    private String endTime;

    private Long salonId;

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

    public Long getSalonId() {
        return salonId;
    }

    public void setSalonId(
            Long salonId
    ) {
        this.salonId = salonId;
    }
}
