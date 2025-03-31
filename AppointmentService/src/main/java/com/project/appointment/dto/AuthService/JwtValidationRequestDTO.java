package com.project.appointment.dto.AuthService;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtValidationRequestDTO {
    String token;
    boolean includeUser;
}
