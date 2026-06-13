package com.example.it211project.controller;

import com.example.it211project.model.dto.request.ForgotPasswordRequest;
import com.example.it211project.model.dto.request.LoginRequest;
import com.example.it211project.model.dto.request.RefreshTokenRequest;
import com.example.it211project.model.dto.request.RegisterRequest;
import com.example.it211project.model.dto.response.LoginResponse;
import com.example.it211project.model.dto.response.UserResponse;
import com.example.it211project.model.dto.response.ApiResponse;
import com.example.it211project.service.AuthService;
import com.example.it211project.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest request) {
        return new ResponseEntity<>(new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Đăng ký thành công",
                userService.register(request)
        ), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        return new ResponseEntity<>(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Đăng nhập thành công",
                authService.login(request)
        ), HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return new ResponseEntity<>(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Token làm mới thành công",
                authService.refreshToken(request)
        ), HttpStatus.OK);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        userService.forgotPassword(request);
        return new ResponseEntity<>(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Đặt lại mật khẩu thành công",
                null
        ), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, "Token không hợp lệ", null));
        }

        String token = authHeader.substring(7);

        authService.logout(token);

        return new ResponseEntity<>(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Logout thành công",
                null
        ), HttpStatus.OK);
    }
}
