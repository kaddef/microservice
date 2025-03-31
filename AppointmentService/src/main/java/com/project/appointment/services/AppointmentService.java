package com.project.appointment.services;

import com.project.appointment.dto.AppointmentRequestDTO;
import com.project.appointment.dto.AuthService.UserDataDTO;
import com.project.appointment.dto.mq.MailMessageDTO;
import com.project.appointment.models.*;
import com.project.appointment.models.enums.AppointmentStatus;
import com.project.appointment.repos.AppointmentRepository;
import com.project.appointment.repos.AvailabilityRepository;
import com.project.appointment.utils.Mapper.AppointmentMapper;
import com.project.appointment.utils.Utility;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final AvailabilityRepository availabilityRepository;
    private final AppointmentMapper appointmentMapper;
    private final MessageService messageService;
    private final AuthServiceClient authServiceClient;


    public AppointmentService(
            AppointmentRepository appointmentRepository,
            AvailabilityRepository availabilityRepository,
            AppointmentMapper appointmentMapper,
            MessageService messageService,
            AuthServiceClient authServiceClient) {
        this.appointmentRepository = appointmentRepository;
        this.availabilityRepository = availabilityRepository;
        this.appointmentMapper = appointmentMapper;
        this.messageService = messageService;
        this.authServiceClient = authServiceClient;
    }

//    public Availability getAvailabilitySettings(Long userId) {
//        return availabilityRepository.findById(userId).orElse(null);
//    }
//
//    public List<DayAvailabilityDTO> getAvailableDaysAndHours(Long userId, LocalDate range_start, LocalDate range_end) {
//        Availability userAvailability = availabilityRepository.findById(userId).orElse(null);
//
//        if (userAvailability == null) {
//            return new ArrayList<>();
//        }
//
//        List<DayAvailabilityDTO> availableDays = new ArrayList<>();
//        LocalDate today = LocalDate.now();
//        int maxDaysAhead = Utility.getMaxDaysAhead(userAvailability.getAdvanceBooking());
//
//        for (int dayOffset = 0; dayOffset < maxDaysAhead; dayOffset++) {
//            LocalDate targetDate = today.plusDays(dayOffset);
//
//            if (!isDayConfigured(targetDate, userAvailability)) {
//                continue;
//            }
//
//            DayAvailabilityDTO dayAvailability = createDayAvailability(targetDate, userAvailability);
//            availableDays.add(dayAvailability);
//        }
//
//        return availableDays;
//    }

    public void createAppointmentRequest (AppointmentRequestDTO appointmentRequestDTO) {
        Availability userAvailability = availabilityRepository.findByUserId(appointmentRequestDTO.getRecipientId()).orElse(null);
        // We shouldn't fall most of these checks because we are going to send only available times and days to the user.
        if (userAvailability == null) {
            System.out.println("Recipient availability not found");
            return ; // This if means somehow availabilitiy is not found
        }

        boolean isConfigurated = isDayConfigured(appointmentRequestDTO.getAppointmentDate(), userAvailability);

        if(!isConfigurated) {
            System.out.println("Day is not configured");
            return ; // In wanted day the doctor does not have a config
        }

        boolean isAvailable = isAvailableAt(appointmentRequestDTO.getAppointmentDate(), appointmentRequestDTO.getAppointmentTime(), userAvailability);

        if(!isAvailable) {
            System.out.println("Recipient is not available");
            return ; // doctor has a config but given time is out of the timeslots
        }

        boolean appointmentConflict = checkAppointmentConflict(
                appointmentRequestDTO.getAppointmentDate(),
                appointmentRequestDTO.getAppointmentTime(),
                appointmentRequestDTO.getRecipientId()
        );

        if(appointmentConflict) {
            System.out.println("Appointment conflict");
            return; // doctor has an appointment in same time
        }

        Appointment appointment = appointmentMapper.toEntitiy(appointmentRequestDTO);
        appointmentRepository.save(appointment);

        // SEND MAIL TO DOCTOR
        UserDataDTO recipient = authServiceClient.getUserDataWithId(appointment.getRecipientId());
        UserDataDTO requester = authServiceClient.getUserDataWithId(appointment.getRequesterId());

        MailMessageDTO message = new MailMessageDTO();
        message.setFrom(requester.getEmail());
        message.setTo(recipient.getEmail());
        message.setSubject("Appointment Request");
        message.setBody(String.format("%s %s requested an appointment", requester.getFirst_name(), requester.getLast_name()));

        try {
            messageService.sendMessageToQueue(message);
        } catch (Exception e) {
            System.out.println("Ya mq ya bağlanamadım ya da mesajı gönderemedim");
        }
    }

    public List<Appointment> getPendingAppointments (Long recipientId) {
        return appointmentRepository.findAppointmentsByRecipientIdAndStatus(recipientId, AppointmentStatus.PENDING);
    }

    public List<Appointment> getMyRecipientAppointments(Long id, AppointmentStatus status) {
        if (status == null) {
            return appointmentRepository.findAppointmentsByRecipientId(id);
        }
        return appointmentRepository.findAppointmentsByRecipientIdAndStatus(id, status);
    }

    public List<Appointment> getMyRequesterAppointments(Long id, AppointmentStatus status) {
        if (status == null) {
            return appointmentRepository.findAppointmentsByRequesterId(id);
        }
        return appointmentRepository.findAppointmentsByRequesterIdAndStatus(id, status);
    }

    public boolean acceptAppointment(Long appointmentId, Long recipientId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        if (appointment == null) {
            System.out.println("Appointment not found");
            return false;
        }
        if(!recipientId.equals(appointment.getRecipientId())) {
            System.out.println("This appointment is not yours to accept");
            return false;
        }
        if(appointment.getStatus() != AppointmentStatus.PENDING) {
            System.out.println("This appointment is already concluded");
            return false;
        }
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        appointmentRepository.save(appointment);

        // SEND MAIL TO PATIENT
        UserDataDTO recipient = authServiceClient.getUserDataWithId(appointment.getRecipientId());
        UserDataDTO requester = authServiceClient.getUserDataWithId(appointment.getRequesterId());

        MailMessageDTO message = new MailMessageDTO();
        message.setFrom(recipient.getEmail());
        message.setTo(requester.getEmail());
        message.setSubject("Appointment Accepted");
        message.setBody(String.format("%s %s accepted your appointment request", recipient.getFirst_name(), recipient.getLast_name()));

        try {
            messageService.sendMessageToQueue(message);
            return true;
        } catch (Exception e) {
            System.out.println("Ya mq ya bağlanamadım ya da mesajı gönderemedim");
            return false;
        }
    }

    public boolean rejectAppointment(Long appointmentId, Long recipientId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        if (appointment == null) {
            System.out.println("Appointment not found");
            return false;
        }
        if(!recipientId.equals(appointment.getRecipientId())) {
            System.out.println("This appointment is not yours to reject");
            return false;
        }
        if(appointment.getStatus() != AppointmentStatus.PENDING) {
            System.out.println("This appointment is already concluded");
            return false;
        }
        appointment.setStatus(AppointmentStatus.REJECTED);
        appointmentRepository.save(appointment);

        // SEND MAIL TO PATIENT
        UserDataDTO recipient = authServiceClient.getUserDataWithId(appointment.getRecipientId());
        UserDataDTO requester = authServiceClient.getUserDataWithId(appointment.getRequesterId());

        MailMessageDTO message = new MailMessageDTO();
        message.setFrom(recipient.getEmail());
        message.setTo(requester.getEmail());
        message.setSubject("Appointment Rejected");
        message.setBody(String.format("%s %s rejected your appointment request", recipient.getFirst_name(), recipient.getLast_name()));

        try {
            messageService.sendMessageToQueue(message);
            return true;
        } catch (Exception e) {
            System.out.println("Ya mq ya bağlanamadım ya da mesajı gönderemedim");
            return false;
        }
    }

    public boolean cancelAppointment(Long appointmentId, Long id) {//This id is the canceller
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        if (appointment == null) {
            System.out.println("Appointment not found");
            return false;
        }
        if(!id.equals(appointment.getRecipientId()) && !id.equals(appointment.getRequesterId())) {
            System.out.println("This appointment is not yours to cancel");
            return false;
        }
        if(appointment.getStatus() == AppointmentStatus.REJECTED) {
            System.out.println("You cannot cancel a rejected appointment");
            return false;
        }
        if(appointment.getStatus() == AppointmentStatus.CANCELLED) {
            System.out.println("This appointment is already cancelled");
            return false;
        }
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);

        // SEND MAIL TO OTHER PART OF THE APPOINTMENT
        UserDataDTO recipient = authServiceClient.getUserDataWithId(appointment.getRecipientId());
        UserDataDTO requester = authServiceClient.getUserDataWithId(appointment.getRequesterId());

        UserDataDTO from = id.equals(appointment.getRequesterId()) ? requester : recipient;
        UserDataDTO to = id.equals(appointment.getRequesterId()) ? recipient : requester;

        MailMessageDTO message = new MailMessageDTO();
        message.setFrom(from.getEmail());
        message.setTo(to.getEmail());
        message.setSubject("Appointment Canceled");
        message.setBody("The appointment scheduled between you and user " + from.getFirst_name() + " has been cancelled.");
        message.setBody(String.format("The appointment scheduled between you and %s %s has been cancelled.", from.getFirst_name(), from.getLast_name()));

        try {
            messageService.sendMessageToQueue(message);
            return true;
        } catch (Exception e) {
            System.out.println("Ya mq ya bağlanamadım ya da mesajı gönderemedim");
            return false;
        }
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

//    /**
//     * Creates a DayAvailabilityDTO for the specified date based on user's availability settings.
//     */
//    private DayAvailabilityDTO createDayAvailability(LocalDate date, Availability availability) {
//        DayAvailabilityDTO dayAvailability = new DayAvailabilityDTO();
//        dayAvailability.setDate(date);
//
//        // Check for excepted days (could extend this to set different statuses)
//        // List<ExceptedDay> exceptedDays = availability.getExceptedDays();
//        // TODO: Handle excepted days logic
//
//        dayAvailability.setStatus("available");
//        dayAvailability.setTimeSlots(generateTimeSlots(date, availability));
//
//        return dayAvailability;
//    }

//    /**
//     * Generates all available time slots for a specific day based on availability configuration.
//     */
//    private List<TimeSlotAvailabilityDTO> generateTimeSlots(LocalDate date, Availability availability) {
//        List<TimeSlot> dayTimeSlots = Utility.getTimeSlotsForDay(date, availability);
//        List<TimeSlotAvailabilityDTO> availableTimeSlots = new ArrayList<>();
//
//        for (TimeSlot timeSlot : dayTimeSlots) {
//            List<TimeSlotAvailabilityDTO> slotBlocks = Utility.getTimeSlotsAvailability(
//                    timeSlot.getStartTime(),
//                    timeSlot.getEndTime(),
//                    availability.getScheduleBlock()
//            );
//            availableTimeSlots.addAll(slotBlocks);
//        }
//
//        return availableTimeSlots;
//    }

    /**
     * Checks if the recipient is available at a given hour and day.
     */
    private boolean isAvailableAt(LocalDate date, LocalTime time, Availability availability) {
        List<TimeSlot> dayTimeSlots = Utility.getTimeSlotsForDay(date, availability);

        for (TimeSlot timeSlot : dayTimeSlots) {
            LocalTime startTime = timeSlot.getStartTime();
            LocalTime endTime = timeSlot.getEndTime();

            if(!time.isBefore(startTime) && time.isBefore(endTime)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if an appointment already exists for the given recipient at the specified date and time.
     * This is used to detect any potential conflicts before creating a new appointment.
     */
    private boolean checkAppointmentConflict(LocalDate appointmentDate, LocalTime appointmentTime, Long recipientId) {
        Optional<Appointment> appointment = appointmentRepository.findByRecipientIdAndAppointmentDateAndAppointmentTime(recipientId, appointmentDate, appointmentTime);
        return appointment.isPresent();
    }
}
