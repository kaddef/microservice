package com.project.appointment.utils;

import com.project.appointment.dto.TimeSlotAvailabilityDTO;
import com.project.appointment.models.Availability;
import com.project.appointment.models.DayOfWeek;
import com.project.appointment.models.TimeSlot;
import com.project.appointment.models.enums.AdvanceBooking;
import com.project.appointment.models.enums.ScheduleBlock;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class Utility {
    public static int getMaxDaysAhead(AdvanceBooking advanceBooking) {
        return switch (advanceBooking) {
            case ONE_WEEK -> 7;
            case ONE_MONTH -> 30;
            case THREE_MONTHS -> 90;
            default -> 30;
        };
    };

    public static List<TimeSlot> getTimeSlotsForDay(LocalDate day, Availability availability) {
        List<TimeSlot> availableTimeSlots = new ArrayList<>();
        List<DayOfWeek> daysOfWeek = availability.getDaysOfWeek();

        for (DayOfWeek dayOfWeek : daysOfWeek) {
            if (dayOfWeek.getDay() == day.getDayOfWeek()) {
                // Day matches, now fetch time slots
                availableTimeSlots.addAll(dayOfWeek.getTimeSlots());
            }
        }

        return availableTimeSlots;
    }

//    public static List<TimeSlotAvailabilityDTO> getTimeSlotsAvailability(LocalTime startTime, LocalTime endTime, ScheduleBlock block) {
//        int blockMinute = getMinuteFromScheduleBlock(block);
//        List<TimeSlotAvailabilityDTO> timeSlotAvailabilities = new ArrayList<>();
//
//        LocalTime currentTime = startTime;
//        while (currentTime.isBefore(endTime)) {
//            // TODO: HERE WE SHOULD CHECK IF THERE IS A APPOINTMENT IN CURRENT TIME
//            timeSlotAvailabilities.add(new TimeSlotAvailabilityDTO(currentTime));
//            currentTime = currentTime.plusMinutes(blockMinute);
//        }
//
//        return timeSlotAvailabilities;
//    }

    public static int getMinuteFromScheduleBlock(ScheduleBlock block) {
        return switch (block) {
            case FIFTEEN_MINUTES -> 15;
            case THIRTY_MINUTES -> 30;
            case ONE_HOUR -> 60;
            default -> 15;
        };
    }
}
