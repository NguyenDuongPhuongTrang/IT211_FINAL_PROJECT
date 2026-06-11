package com.example.it211project.test_service;

import com.example.it211project.enums.Role;
import com.example.it211project.exception.ConflictException;
import com.example.it211project.exception.ResourceNotFoundException;
import com.example.it211project.exception.UnauthorizedException;
import com.example.it211project.model.dto.request.ChangePasswordRequest;
import com.example.it211project.model.dto.request.RegisterRequest;
import com.example.it211project.model.dto.response.UserResponse;
import com.example.it211project.model.entity.User;
import com.example.it211project.repository.UserRepository;
import com.example.it211project.service.impl.UserServiceImpl;
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
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void register_success() {

        RegisterRequest request = new RegisterRequest();
        request.setUsername("newuser");
        request.setEmail("new@gmail.com");
        request.setPassword("123456");

        when(userRepository.existsByUsername("newuser"))
                .thenReturn(false);

        when(userRepository.existsByEmail("new@gmail.com"))
                .thenReturn(false);

        when(passwordEncoder.encode("123456"))
                .thenReturn("encoded-password");

        UserResponse response = userService.register(request);

        assertNotNull(response);
        assertEquals("newuser", response.getUsername());

        verify(userRepository, times(1))
                .save(any(User.class));
    }

    @Test
    void register_username_exists() {

        RegisterRequest request = new RegisterRequest();
        request.setUsername("trang");

        when(userRepository.existsByUsername("trang"))
                .thenReturn(true);

        assertThrows(
                ConflictException.class,
                () -> userService.register(request)
        );

        verify(userRepository, never())
                .save(any(User.class));
    }

    @Test
    void register_email_exists() {

        RegisterRequest request = new RegisterRequest();
        request.setUsername("newuser");
        request.setEmail("trang@gmail.com");

        when(userRepository.existsByUsername("newuser"))
                .thenReturn(false);

        when(userRepository.existsByEmail("trang@gmail.com"))
                .thenReturn(true);

        assertThrows(
                ConflictException.class,
                () -> userService.register(request)
        );
    }

    @Test
    void getUserById_success() {

        User user = User.builder()
                .id(1L)
                .username("ptrang")
                .email("ptrang@gmail.com")
                .role(Role.ROLE_ADMIN)
                .active(true)
                .build();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        UserResponse response =
                userService.getUserById(1L);

        assertEquals("ptrang", response.getUsername());
        assertEquals(Role.ROLE_ADMIN, response.getRole());
    }

    @Test
    void getUserById_not_found() {

        when(userRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getUserById(100L)
        );
    }

    @Test
    void changePassword_success() {

        ChangePasswordRequest request =
                new ChangePasswordRequest();

        request.setOldPassword("123456");
        request.setNewPassword("654321");

        User user = User.builder()
                .username("trang")
                .password("encodedOld")
                .build();

        when(userRepository.findByUsername("trang"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "123456",
                "encodedOld"
        )).thenReturn(true);

        when(passwordEncoder.encode("654321"))
                .thenReturn("encodedNew");

        userService.changePassword("trang", request);

        verify(userRepository)
                .save(any(User.class));
    }

    @Test
    void changePassword_wrong_old_password() {

        ChangePasswordRequest request =
                new ChangePasswordRequest();

        request.setOldPassword("wrong");
        request.setNewPassword("654321");

        User user = User.builder()
                .username("trang")
                .password("encodedOld")
                .build();

        when(userRepository.findByUsername("trang"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "wrong",
                "encodedOld"
        )).thenReturn(false);

        assertThrows(
                UnauthorizedException.class,
                () -> userService.changePassword("trang", request)
        );
    }
}
