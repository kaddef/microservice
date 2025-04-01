package com.project.appointment.controllers;

import com.project.appointment.dto.AvailabilitySettingDTO;
import com.project.appointment.dto.DayAvailabilityDTO;
import com.project.appointment.services.AppointmentService;
import com.project.appointment.services.AvailabilityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/v1/availability")
public class AvailabilityController {
    private final AppointmentService appointmentService;
    private final AvailabilityService availabilityService;

    public AvailabilityController(AppointmentService appointmentService, AvailabilityService availabilityService) {
        this.appointmentService = appointmentService;
        this.availabilityService = availabilityService;
    }

    @GetMapping("/{userId}/settings")
    public ResponseEntity<?> getAvailabilitySettings(@PathVariable Long userId) {
        return ResponseEntity.ok(availabilityService.getAvailabilitySettings(userId));
    }

    @GetMapping("/{userId}/days")
    public ResponseEntity<?> getAvailableDaysAndHours(
            @PathVariable Long userId,
            @RequestParam LocalDate range_start,
            @RequestParam LocalDate range_end
    ) {
        System.out.println(range_start);
        System.out.println(range_end);
        List<DayAvailabilityDTO> availableDaysAndHours = availabilityService.getAvailableDaysAndHours(userId, range_start, range_end);
        return ResponseEntity.ok(availableDaysAndHours);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateAvailabilitySettings(@RequestBody AvailabilitySettingDTO availabilitySettingDTO) {
        Long id = (long) 10; // We are going to get this id from jwt after we authenticate user from authservice
        boolean result = availabilityService.updateAvailabilitySettings(availabilitySettingDTO, id);
        return ResponseEntity.ok("Data updated successfully");
    }
}
