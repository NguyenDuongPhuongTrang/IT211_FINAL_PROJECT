package com.example.it211project.service;

import com.example.it211project.model.dto.request.LoginRequest;
import com.example.it211project.model.dto.request.RefreshTokenRequest;
import com.example.it211project.model.dto.response.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    LoginResponse refreshToken(RefreshTokenRequest request);
    void logout(String token);
}
