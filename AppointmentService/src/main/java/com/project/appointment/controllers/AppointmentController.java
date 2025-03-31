package com.project.appointment.controllers;

import com.project.appointment.dto.AppointmentRequestDTO;
import com.project.appointment.models.Appointment;
import com.project.appointment.services.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/appointment")
public class AppointmentController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping()
    public ResponseEntity<?> createAppointmentRequest(@RequestBody AppointmentRequestDTO appointmentRequestDTO) {
        this.appointmentService.createAppointmentRequest(appointmentRequestDTO);
        return ResponseEntity.ok("Well done");
    }

    @GetMapping("/pendings")
    public ResponseEntity<List<Appointment>> getPendingAppointments() {
        Long id = (long) 10; // We are going to get this id from jwt after we authenticate user from authservice
        List<Appointment> pendingAppointments = this.appointmentService.getPendingAppointments(id);
        return ResponseEntity.ok(pendingAppointments);
    }

    @GetMapping("/me/requester")
    public ResponseEntity<List<Appointment>> getMyRequesterAppointments() {
        Long id = (long) 10; // We are going to get this id from jwt after we authenticate user from authservice
        List<Appointment> myAppointments = this.appointmentService.getMyRequesterAppointments(id, null);
        return ResponseEntity.ok(myAppointments);
    }

    @GetMapping("/me/recipient")
    public ResponseEntity<List<Appointment>> getMyRecipientAppointments() {
        Long id = (long) 10; // We are going to get this id from jwt after we authenticate user from authservice
        List<Appointment> myAppointments = this.appointmentService.getMyRecipientAppointments(id, null);
        return ResponseEntity.ok(myAppointments);
    }

    @PostMapping("/accept/{appointmentId}")
    public ResponseEntity<?> acceptAppointment(@PathVariable Long appointmentId) {
        Long id = (long) 10; // We are going to get this id from jwt after we authenticate user from authservice
        boolean isAccepted = this.appointmentService.acceptAppointment(appointmentId, id);
        if(isAccepted) {
            return ResponseEntity.ok("Successfully accepted");
        } else {
            return ResponseEntity.ok("Failed to accept");
        }
    }

    @PostMapping("/reject/{appointmentId}")
    public ResponseEntity<?> rejectAppointment(@PathVariable Long appointmentId) {
        Long id = (long) 10; // We are going to get this id from jwt after we authenticate user from authservice
        boolean isRejected = this.appointmentService.rejectAppointment(appointmentId, id);
        if(isRejected) {
            return ResponseEntity.ok("Successfully rejected");
        } else {
            return ResponseEntity.ok("Failed to reject");
        }
    }

    @PostMapping("/cancel/{appointmentId}")
    public ResponseEntity<?> cancelAppointment(@PathVariable Long appointmentId) {
        Long id = (long) 9; // We are going to get this id from jwt after we authenticate user from authservice
        boolean isCancelled = this.appointmentService.cancelAppointment(appointmentId, id);
        if(isCancelled) {
            return ResponseEntity.ok("Successfully canceled");
        } else {
            return ResponseEntity.ok("Failed to cancel");
        }
    }
}
