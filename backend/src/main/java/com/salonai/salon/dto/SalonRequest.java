package com.salonai.salon.dto;

public class SalonRequest {

    private String name;
    private String address;
    private String phone;
    private String openTime;
    private String closeTime;

    public String getName() {
        return name;
    }

    public void setName(
            String name
    ) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(
            String address
    ) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(
            String phone
    ) {
        this.phone = phone;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(
            String openTime
    ) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(
            String closeTime
    ) {
        this.closeTime = closeTime;
    }
}