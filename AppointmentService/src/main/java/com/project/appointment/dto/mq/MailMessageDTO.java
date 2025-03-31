package com.project.appointment.dto.mq;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailMessageDTO {
    private String from;
    private String to;
    private String subject;
    private String body;
}


