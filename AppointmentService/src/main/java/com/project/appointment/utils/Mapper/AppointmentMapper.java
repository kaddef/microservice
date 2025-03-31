package com.project.appointment.utils.Mapper;

import com.project.appointment.dto.AppointmentRequestDTO;
import com.project.appointment.models.Appointment;
import com.project.appointment.models.enums.AppointmentStatus;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {
    public Appointment toEntitiy(AppointmentRequestDTO appointmentRequestDTO) {
        Appointment appointment = new Appointment();
        appointment.setRequesterId(appointmentRequestDTO.getRequesterId());
        appointment.setRecipientId(appointmentRequestDTO.getRecipientId());
        appointment.setAppointmentDate(appointmentRequestDTO.getAppointmentDate());
        appointment.setAppointmentTime(appointmentRequestDTO.getAppointmentTime());
        appointment.setStatus(AppointmentStatus.PENDING);
        appointment.setNote(appointmentRequestDTO.getNote());

        return appointment;
    }
}
