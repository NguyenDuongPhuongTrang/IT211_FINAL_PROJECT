package com.example.it211project.test_controller;

import com.example.it211project.controller.AuthController;
import com.example.it211project.exception.CustomAccessDeniedHandler;
import com.example.it211project.exception.CustomAuthenticationEntryPoint;
import com.example.it211project.model.dto.request.LoginRequest;
import com.example.it211project.model.dto.request.RefreshTokenRequest;
import com.example.it211project.model.dto.request.RegisterRequest;
import com.example.it211project.model.dto.response.LoginResponse;
import com.example.it211project.model.dto.response.UserResponse;
import com.example.it211project.security.jwt.JwtAuthenticationFilter;
import com.example.it211project.service.AuthService;
import com.example.it211project.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest(AuthController.class)
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    tools.jackson.databind.ObjectMapper objectMapper;

    @MockitoBean
    UserService userService;

    @MockitoBean
    AuthService authService;

    @MockitoBean
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    CustomAccessDeniedHandler customAccessDeniedHandler;

    @MockitoBean
    CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Test
    void register_Success() throws Exception {

        RegisterRequest request = new RegisterRequest();
        request.setUsername("trang");
        request.setEmail("trang@gmail.com");
        request.setPassword("123456");

        UserResponse response = UserResponse.builder()
                .id(1L)
                .username("trang")
                .email("trang@gmail.com")
                .build();

        when(userService.register(any(RegisterRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void login_Success() throws Exception {

        LoginRequest request = new LoginRequest();
        request.setUsername("trang");
        request.setPassword("123456");

        when(authService.login(any(LoginRequest.class)))
                .thenReturn(new LoginResponse());

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void refreshToken_Success() throws Exception {

        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("token");

        when(authService.refreshToken(any(RefreshTokenRequest.class)))
                .thenReturn(new LoginResponse());

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}