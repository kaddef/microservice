package com.project.appointment.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class TimeSlotAvailabilityDTO {
    public TimeSlotAvailabilityDTO(LocalTime time) {
        this.startTime = time;
        this.status = "available";
    }

    private LocalTime startTime;
    private String status;
}

