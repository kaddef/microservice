package com.project.appointment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class AvailabilitySettingDTO {
    private List<DayOfWeekDTO> days;

    @Getter
    @Setter
    public static class DayOfWeekDTO {
        private java.time.DayOfWeek day;
        private List<TimeSlotDTO> timeSlots;

        @Getter
        @Setter
        public static class TimeSlotDTO {
            private LocalTime startTime;
            private LocalTime endTime;
        }
    }
}
