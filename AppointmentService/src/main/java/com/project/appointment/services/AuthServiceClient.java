package com.project.appointment.services;

import com.project.appointment.dto.AuthService.JwtValidationRequestDTO;
import com.project.appointment.dto.AuthService.JwtValidationResponseDTO;
import com.project.appointment.dto.AuthService.UserDataDTO;
import com.project.appointment.dto.AuthService.UserDataResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class AuthServiceClient {
    private final RestTemplate restTemplate;
    @Value("${auth.service.url}")
    private String authServiceUrl;

    AuthServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean checkJwt(String token) {
        System.out.println("token: " + token);
        JwtValidationRequestDTO request = new JwtValidationRequestDTO();
        request.setToken(token);
        request.setIncludeUser(false);

        HttpEntity<JwtValidationRequestDTO> requestEntity = new HttpEntity<>(request);

        try {
            ResponseEntity<JwtValidationResponseDTO> response = restTemplate.exchange(
                    authServiceUrl + "/api/auth/verify",
                    HttpMethod.POST,
                    requestEntity,
                    JwtValidationResponseDTO.class,
                    Collections.emptyMap());

            if (response.getBody() != null) {
                return response.getBody().isValid();
            } else {
                System.out.println("Returning Default Value");
                return false;
            }
        } catch (HttpClientErrorException e) {
            System.out.println("A 4xx error occurred");
            return false;
        } catch (HttpServerErrorException e) {
            System.out.println("A 5xx error occurred");
            return false;
        }
    }

    public UserDataDTO getUserDataWithId(Long id) {
        try {
            ResponseEntity<UserDataResponseDTO> response = restTemplate.exchange(
                    authServiceUrl + "/api/users/" + id,
                    HttpMethod.GET,
                    null,
                    UserDataResponseDTO.class,
                    Collections.emptyMap());

            if (response.getBody() != null) {
                return response.getBody().getData();
            } else {
                System.out.println("Returning Default Value");
                return new UserDataDTO();
            }
        } catch (HttpClientErrorException e) {
            System.out.println("A 4xx error occurred");
            return new UserDataDTO();
        } catch (HttpServerErrorException e) {
            System.out.println("A 5xx error occurred");
            return new UserDataDTO();
        }
    }
}
