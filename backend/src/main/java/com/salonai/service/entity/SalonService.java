package com.salonai.service.entity;

import com.salonai.salon.entity.Salon;
import jakarta.persistence.*;

@Entity
@Table(name = "services")
public class SalonService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Double price;

    private Integer duration;

    @ManyToOne
    @JoinColumn(name = "salon_id")
    private Salon salon;

    public SalonService() {
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(
            Double price
    ) {

        this.price = price;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(
            Integer duration
    ) {

        this.duration = duration;
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