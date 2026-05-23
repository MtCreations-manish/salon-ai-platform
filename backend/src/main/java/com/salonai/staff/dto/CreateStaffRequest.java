package com.salonai.staff.dto;

public class CreateStaffRequest {

    private String name;

    private String role;

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