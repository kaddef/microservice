package com.project.appointment.dto;

import com.project.appointment.models.TimeSlot;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class DayAvailabilityDTO {
    private LocalDate date;
    private String status;
    private List<TimeSlotAvailabilityDTO> timeSlots;
}
