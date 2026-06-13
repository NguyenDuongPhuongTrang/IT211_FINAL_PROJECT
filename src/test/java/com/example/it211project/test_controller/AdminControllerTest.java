package com.example.it211project.test_controller;

import com.example.it211project.controller.AdminController;
import com.example.it211project.enums.Role;
import com.example.it211project.model.dto.request.UserRequest;
import com.example.it211project.model.dto.response.UserResponse;
import com.example.it211project.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
class AdminControllerTest {

    @Autowired
    WebApplicationContext context;

    MockMvc mockMvc;

    @MockitoBean
    UserService userService;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    private UserResponse sampleUser() {
        return UserResponse.builder()
                .id(1L)
                .username("admin_user")
                .email("admin@gmail.com")
                .role(Role.ROLE_CUSTOMER)
                .active(true)
                .build();
    }

    @Test
    void getUsers_Success() throws Exception {
        var page = new PageImpl<>(List.of(sampleUser()), PageRequest.of(0, 5), 1);
        when(userService.getUsers(anyString(), anyInt(), anyInt())).thenReturn(page);

        mockMvc.perform(get("/api/v1/admin/users")
                        .with(user("admin").roles("ADMIN"))
                        .param("keyword", "")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    void getUserById_Success() throws Exception {
        when(userService.getUserById(1L)).thenReturn(sampleUser());

        mockMvc.perform(get("/api/v1/admin/users/1")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    void updateUser_Success() throws Exception {
        UserResponse updated = UserResponse.builder()
                .id(1L)
                .username("user_updated")
                .email("updated@gmail.com")
                .role(Role.ROLE_CUSTOMER)
                .active(true)
                .build();

        when(userService.updateUser(eq(1L), any(UserRequest.class))).thenReturn(updated);

        String requestJson = """
                {
                    "username": "user_updated",
                    "email": "updated@gmail.com",
                    "role": "ROLE_CUSTOMER",
                    "active": true
                }
                """;

        mockMvc.perform(put("/api/v1/admin/users/1")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.username").value("user_updated"));
    }

    @Test
    void updateUser_InvalidRequest_Return400() throws Exception {
        String requestJson = """
                {
                    "username": "",
                    "email": "not-an-email"
                }
                """;

        mockMvc.perform(put("/api/v1/admin/users/1")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteUser_Success() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/v1/admin/users/1")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.status").value(204))
                .andExpect(jsonPath("$.message").value("Xóa người dùng thành công"));
    }
}