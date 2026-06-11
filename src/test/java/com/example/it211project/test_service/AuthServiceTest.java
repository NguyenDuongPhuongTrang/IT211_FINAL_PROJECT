package com.example.it211project.test_service;

import com.example.it211project.enums.Role;
import com.example.it211project.exception.UnauthorizedException;
import com.example.it211project.model.dto.request.LoginRequest;
import com.example.it211project.model.dto.response.LoginResponse;
import com.example.it211project.model.entity.User;
import com.example.it211project.repository.TokenBlacklistRepository;
import com.example.it211project.repository.UserRepository;
import com.example.it211project.security.jwt.JwtService;
import com.example.it211project.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private TokenBlacklistRepository tokenBlacklistRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void login_Success() {

        LoginRequest request = new LoginRequest();
        request.setUsername("trang");
        request.setPassword("123456");

        User user = User.builder()
                .id(1L)
                .username("trang")
                .password("encoded")
                .role(Role.ROLE_CUSTOMER)
                .build();

        when(userRepository.findByUsername("trang"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("123456", "encoded"))
                .thenReturn(true);

        when(jwtService.generateAccessToken(user))
                .thenReturn("access-token");

        when(jwtService.generateRefreshToken(user))
                .thenReturn("refresh-token");

        LoginResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());
    }

    @Test
    void login_WrongPassword_ThrowUnauthorizedException() {

        LoginRequest request = new LoginRequest();
        request.setUsername("trang");
        request.setPassword("wrong");

        User user = User.builder()
                .username("trang")
                .password("encoded")
                .build();

        when(userRepository.findByUsername("trang"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("wrong", "encoded"))
                .thenReturn(false);

        assertThrows(
                UnauthorizedException.class,
                () -> authService.login(request)
        );
    }
}
