package com.project.appointment.models.V2;

import com.project.appointment.models.enums.AdvanceBooking;
import com.project.appointment.models.enums.ScheduleBlock;
import jakarta.persistence.*;


public class AppointmentAvailability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long ownerId;

    @Enumerated(EnumType.STRING)
    private AdvanceBooking advanceBooking;

    @Enumerated(EnumType.STRING)
    private ScheduleBlock scheduleBlock;
}
