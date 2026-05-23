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