package com.salonai.salon.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "salons")
public class Salon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String salonName;

    private String description;

    private String city;

    private String area;

    private String address;

    private String phone;

    private String openingTime;

    private String closingTime;

    private Integer maxBookingCapacity = 5;

    private String imageUrl;

    public Salon() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSalonName() {
        return salonName;
    }

    public void setSalonName(String salonName) {
        this.salonName = salonName;
    }

    public void setName(String name) {
        this.salonName = name;
    }

    public String getName() {
        return salonName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getOpenTime() {
        return openingTime;
    }

    public void setOpenTime(String openTime) {
        this.openingTime = openTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public String getCloseTime() {
        return closingTime;
    }

    public void setCloseTime(String closeTime) {
        this.closingTime = closeTime;
    }

    public Integer getMaxBookingCapacity() {
        return maxBookingCapacity;
    }

    public void setMaxBookingCapacity(Integer maxBookingCapacity) {
        this.maxBookingCapacity = maxBookingCapacity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
