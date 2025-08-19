package com.khoi.service;


import com.khoi.domain.USER_ROLE;
import com.khoi.dto.request.LoginRequest;
import com.khoi.dto.response.AuthResponse;
import com.khoi.dto.request.SignupRequest;
import org.springframework.stereotype.Component;

public interface AuthService {
    void sentLoginOtp(String email, USER_ROLE role) throws Exception;
    String createUser(SignupRequest request) throws Exception;
    AuthResponse signing(LoginRequest request) throws Exception;
}
