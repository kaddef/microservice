package com.project.appointment.dto.AuthService;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDataResponseDTO {
    private UserDataDTO data;
    private String error;
}
