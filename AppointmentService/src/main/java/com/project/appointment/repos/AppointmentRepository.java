package com.project.appointment.repos;

import com.project.appointment.models.Appointment;
import com.project.appointment.models.enums.AppointmentStatus;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends CrudRepository<Appointment, Long> {
    Optional<Appointment> findByRecipientIdAndAppointmentDateAndAppointmentTime(
            Long recipientId, LocalDate appointmentDate, LocalTime appointmentTime
    );

    List<Appointment> findAppointmentsByRecipientId(Long recipientId);

    List<Appointment> findAppointmentsByRequesterId(Long recipientId);

    List<Appointment> findAppointmentsByRequesterIdAndStatus(
            Long recipientId, AppointmentStatus status
    );

    List<Appointment> findAppointmentsByRecipientIdAndStatus(
            Long recipientId, AppointmentStatus status
    );

    List<Appointment> findAppointmentsByAppointmentDate(LocalDate date);
}
