package com.khoi.controller;

import com.khoi.domain.USER_ROLE;
import com.khoi.dto.request.LoginOtpRequest;
import com.khoi.dto.request.LoginRequest;
import com.khoi.entity.VerificationCode;
import com.khoi.repository.UserRepository;
import com.khoi.dto.response.ApiResponse;
import com.khoi.dto.response.AuthResponse;
import com.khoi.dto.request.SignupRequest;
import com.khoi.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor // tự động tạo 1 constructor cho biến repository dưới, nếu ko thì phải viết constructor thủ công
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignupRequest request) throws Exception {

        String token = authService.createUser(request);

        AuthResponse res = new AuthResponse();
        res.setToken(token);
        res.setMessage("Register success");
        res.setRole(USER_ROLE.ROLE_CUSTOMER);

        return ResponseEntity
                .status(200)
                .body(res);
    }

    @PostMapping("/sent/login-signup-otp")
    public ResponseEntity<ApiResponse> sentOtpHandler(
            @RequestBody LoginOtpRequest request
    ) throws Exception {
        authService.sentLoginOtp(request.getEmail(), request.getRole());

        ApiResponse res = new ApiResponse();
        res.setMessage("OTP sent successfully");

        return ResponseEntity
                .status(200)
                .body(res);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signinHandler(@RequestBody LoginRequest request) throws Exception {

        AuthResponse authResponse = authService.signing(request);

        return ResponseEntity
                .status(200)
                .body(authResponse);
    }
}
