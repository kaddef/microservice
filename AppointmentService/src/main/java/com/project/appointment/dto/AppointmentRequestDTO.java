package com.project.appointment.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class AppointmentRequestDTO {
    private Long requesterId;  // The user requesting the appointment #Patient

    private Long recipientId; // #Doctor

    private LocalDate appointmentDate;

    private LocalTime appointmentTime;

    private String note;
}
