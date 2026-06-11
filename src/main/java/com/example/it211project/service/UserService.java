package com.example.it211project.service;

import com.example.it211project.model.dto.request.ChangePasswordRequest;
import com.example.it211project.model.dto.request.RegisterRequest;
import com.example.it211project.model.dto.request.UserRequest;
import com.example.it211project.model.dto.response.UserResponse;
import org.springframework.data.domain.Page;

public interface UserService {
    UserResponse register(RegisterRequest request);
    Page<UserResponse> getUsers(String keyword, int page, int size);
    UserResponse getUserById(Long id);
    UserResponse updateUser(Long id, UserRequest request);
    void deleteUser(Long id);
    void changePassword(String username, ChangePasswordRequest request);
}
