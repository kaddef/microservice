package com.project.appointment.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter

@Entity
@Table(name = "excepted_day")
public class ExceptedDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne
//    @JoinColumn(name = "availability_id", nullable = false)
//    private Availability availability;

    @Column(name = "availability_id")
    private Long availabilityId;

    @Column(nullable = false)
    private LocalDate date;

    private String reason;
}
