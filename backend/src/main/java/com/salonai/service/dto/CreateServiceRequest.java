package com.salonai.service.dto;

public class CreateServiceRequest {

    private String name;

    private Integer durationMinutes;

    private Double price;

    private Long salonId;

    public String getName() {
        return name;
    }

    public void setName(
            String name
    ) {
        this.name = name;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(
            Integer durationMinutes
    ) {
        this.durationMinutes = durationMinutes;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(
            Double price
    ) {
        this.price = price;
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