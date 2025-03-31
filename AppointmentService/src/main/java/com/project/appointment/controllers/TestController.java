package com.project.appointment.controllers;

import com.project.appointment.models.Appointment;
import com.project.appointment.repos.AppointmentRepository;
import com.project.appointment.repos.AvailabilityRepository;
import com.project.appointment.services.AppointmentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {

    private final AppointmentRepository appointmentRepository;
    private final AvailabilityRepository availabilityRepository;

    public TestController(AppointmentRepository appointmentRepository, AvailabilityRepository availabilityRepository) {
        this.appointmentRepository = appointmentRepository;
        this.availabilityRepository = availabilityRepository;
    }

    @GetMapping
    public void Test() {
        availabilityRepository.deleteById((long)2);
    }
}
