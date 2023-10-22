package com.hospitalservice.mkobo.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@Table(name = "patient")
public class Patient implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String firstName;
    private String lastName;
    private int age;
    @Temporal(TemporalType.TIMESTAMP)
    private Date last_visit_date;

    public Patient() {
    }

    public Patient(Long id, String firstName, String lastName, int age, Date last_visit_date) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.last_visit_date = last_visit_date;
    }
}
