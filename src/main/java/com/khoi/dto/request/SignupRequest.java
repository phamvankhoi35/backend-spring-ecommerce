package com.khoi.dto.request;

import lombok.Data;

@Data
public class SignupRequest {
    private String email;
    private String fullname;
    private String otp;
}
