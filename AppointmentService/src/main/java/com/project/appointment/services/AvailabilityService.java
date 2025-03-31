package com.project.appointment.services;

import com.project.appointment.dto.AvailabilitySettingDTO;
import com.project.appointment.dto.DayAvailabilityDTO;
import com.project.appointment.dto.TimeSlotAvailabilityDTO;
import com.project.appointment.models.Appointment;
import com.project.appointment.models.Availability;
import com.project.appointment.models.DayOfWeek;
import com.project.appointment.models.TimeSlot;
import com.project.appointment.models.enums.ScheduleBlock;
import com.project.appointment.repos.AppointmentRepository;
import com.project.appointment.repos.AvailabilityRepository;
import com.project.appointment.utils.Utility;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final AppointmentRepository appointmentRepository;

    public AvailabilityService(AvailabilityRepository availabilityRepository, AppointmentRepository appointmentRepository) {
        this.availabilityRepository = availabilityRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public Availability getAvailabilitySettings(Long userId) {
        return availabilityRepository.findById(userId).orElse(null);
    }

    public List<DayAvailabilityDTO> getAvailableDaysAndHours(Long userId, LocalDate range_start, LocalDate range_end) {
        Availability userAvailability = availabilityRepository.findById(userId).orElse(null);

        if (userAvailability == null) {
            return new ArrayList<>();
        }

        List<DayAvailabilityDTO> availableDays = new ArrayList<>();
        LocalDate today = LocalDate.now();
        int maxDaysAhead = Utility.getMaxDaysAhead(userAvailability.getAdvanceBooking());

        for (int dayOffset = 0; dayOffset < maxDaysAhead; dayOffset++) {
            LocalDate targetDate = today.plusDays(dayOffset);

            if (!isDayConfigured(targetDate, userAvailability)) {
                continue;
            }

            DayAvailabilityDTO dayAvailability = createDayAvailability(targetDate, userAvailability);
            availableDays.add(dayAvailability);
        }

        return availableDays;
    }

    public boolean updateAvailabilitySettings(AvailabilitySettingDTO availabilitySettingDTO, Long id) {
        Availability availability = availabilityRepository.findByUserId(id).orElse(null);
        if(availability == null) {
            System.out.println("Availability not found");
            return false;
        }
        List<DayOfWeek> dayOfWeeks = availability.getDaysOfWeek();
        dayOfWeeks.clear(); // TODO: Maybe dont clear the list update it instead

        for (var day : availabilitySettingDTO.getDays()) {
            DayOfWeek dayOfWeek = new DayOfWeek();
            dayOfWeek.setDay(day.getDay());

            List<TimeSlot> timeSlots = new ArrayList<>();
            for (var timeSlot : day.getTimeSlots()) {
                TimeSlot newTimeSlot = new TimeSlot();
                newTimeSlot.setStartTime(timeSlot.getStartTime());
                newTimeSlot.setEndTime(timeSlot.getEndTime());
                timeSlots.add(newTimeSlot);
            }
            dayOfWeek.setTimeSlots(timeSlots);
            dayOfWeeks.add(dayOfWeek);
        }
        availabilityRepository.save(availability);
        return true;
    }

    /**
     * Checks if the given date has a configuration in the user's availability.
     */
    private boolean isDayConfigured(LocalDate date, Availability availability) {
        for (DayOfWeek configuredDay : availability.getDaysOfWeek()) {
            if (date.getDayOfWeek() == configuredDay.getDay()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a DayAvailabilityDTO for the specified date based on user's availability settings.
     */
    private DayAvailabilityDTO createDayAvailability(LocalDate date, Availability availability) {
        DayAvailabilityDTO dayAvailability = new DayAvailabilityDTO();
        dayAvailability.setDate(date);

        // Check for excepted days (could extend this to set different statuses)
        // List<ExceptedDay> exceptedDays = availability.getExceptedDays();
        // TODO: Handle excepted days logic

        dayAvailability.setStatus("available");
        dayAvailability.setTimeSlots(generateTimeSlots(date, availability));

        return dayAvailability;
    }

    /**
     * Generates all available time slots for a specific day based on availability configuration.
     */
    private List<TimeSlotAvailabilityDTO> generateTimeSlots(LocalDate date, Availability availability) {
        List<TimeSlot> dayTimeSlots = Utility.getTimeSlotsForDay(date, availability);
        List<TimeSlotAvailabilityDTO> availableTimeSlots = new ArrayList<>();

        for (TimeSlot timeSlot : dayTimeSlots) {
            List<TimeSlotAvailabilityDTO> slotBlocks = getTimeSlotsAvailability(
                    timeSlot.getStartTime(),
                    timeSlot.getEndTime(),
                    availability.getScheduleBlock()
            );
            availableTimeSlots.addAll(slotBlocks);
        }

        return availableTimeSlots;
    }

    /**
     * Generates a list of available time slots between the specified start and end times,
     * based on the given schedule block duration.
     */
    public static List<TimeSlotAvailabilityDTO> getTimeSlotsAvailability(LocalTime startTime, LocalTime endTime, ScheduleBlock block) {
        int blockMinute = Utility.getMinuteFromScheduleBlock(block);
        List<TimeSlotAvailabilityDTO> timeSlotAvailabilities = new ArrayList<>();

        LocalTime currentTime = startTime;
        while (currentTime.isBefore(endTime)) {
            // TODO: HERE WE SHOULD CHECK IF THERE IS A APPOINTMENT IN CURRENT TIME
            timeSlotAvailabilities.add(new TimeSlotAvailabilityDTO(currentTime));
            currentTime = currentTime.plusMinutes(blockMinute);
        }

        return timeSlotAvailabilities;
    }

    public List<LocalTime> getAppointmentsInGivenDay(LocalDate date, Long availabilityId) {
        List<LocalTime> appointmentsTimes = new ArrayList<>();
        List<Appointment> appointments = appointmentRepository.findAppointmentsByAppointmentDate(date);

        for (Appointment appointment : appointments) {
            appointmentsTimes.add(appointment.getAppointmentTime());
        }

        return appointmentsTimes;
    }

//    /**
//     * Checks if the recipient is available at a given hour and day.
//     */
//    private boolean isAvailableAt(LocalDate date, LocalTime time, Availability availability) {
//        List<TimeSlot> dayTimeSlots = Utility.getTimeSlotsForDay(date, availability);
//
//        for (TimeSlot timeSlot : dayTimeSlots) {
//            LocalTime startTime = timeSlot.getStartTime();
//            LocalTime endTime = timeSlot.getEndTime();
//
//            if(!time.isBefore(startTime) && time.isBefore(endTime)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    /**
//     * Checks if an appointment already exists for the given recipient at the specified date and time.
//     * This is used to detect any potential conflicts before creating a new appointment.
//     */
//    private boolean checkAppointmentConflict(LocalDate appointmentDate, LocalTime appointmentTime, Long recipientId) {
//        Optional<Appointment> appointment = appointmentRepository.findByRecipientIdAndAppointmentDateAndAppointmentTime(recipientId, appointmentDate, appointmentTime);
//        return appointment.isPresent();
//    }
}
