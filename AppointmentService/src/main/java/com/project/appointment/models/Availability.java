package com.project.appointment.models;

import com.project.appointment.models.enums.AdvanceBooking;
import com.project.appointment.models.enums.ScheduleBlock;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

@Entity
@Table(name = "availability")
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "availability_id", nullable = false)
    private List<DayOfWeek> daysOfWeek;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "availability_id")
    private List<ExceptedDay> exceptedDays;

    @Enumerated(EnumType.STRING)
    private AdvanceBooking advanceBooking;

    @Enumerated(EnumType.STRING)
    private ScheduleBlock scheduleBlock;
}
