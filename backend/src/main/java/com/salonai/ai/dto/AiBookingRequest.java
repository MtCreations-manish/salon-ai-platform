package com.salonai.ai.dto;

import java.time.LocalDate;

public class AiBookingRequest {

    private String city;

    private String serviceName;

    private Long salonId;

    private String salonName;

    private LocalDate bookingDate;

    private String bookingTime;

    private String phoneNumber;

    private String currentStep;

    // =========================================
    // CITY
    // =========================================

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    // =========================================
    // SERVICE
    // =========================================

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    // =========================================
    // SALON ID
    // =========================================

    public Long getSalonId() {
        return salonId;
    }

    public void setSalonId(Long salonId) {
        this.salonId = salonId;
    }

    // =========================================
    // SALON NAME
    // =========================================

    public String getSalonName() {
        return salonName;
    }

    public void setSalonName(String salonName) {
        this.salonName = salonName;
    }

    // =========================================
    // BOOKING DATE
    // =========================================

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    // =========================================
    // BOOKING TIME
    // =========================================

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    // =========================================
    // PHONE NUMBER
    // =========================================

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // =========================================
    // CURRENT STEP
    // =========================================

    public String getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(String currentStep) {
        this.currentStep = currentStep;
    }
}