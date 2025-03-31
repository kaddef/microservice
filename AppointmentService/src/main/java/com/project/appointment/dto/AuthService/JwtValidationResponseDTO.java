package com.project.appointment.dto.AuthService;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JwtValidationResponseDTO {
    private boolean valid;
    private String message;
    private String error;
    private List<String> badFields;
    private UserDataDTO userData;
}
