package com.example.it211project.controller;

import com.example.it211project.model.dto.request.UserRequest;
import com.example.it211project.model.dto.response.ApiResponse;
import com.example.it211project.model.dto.response.UserResponse;
import com.example.it211project.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getUsers(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return new ResponseEntity<>(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Lấy danh sách người dùng thành công",
                userService.getUsers(keyword, page, size)
        ), HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        return new ResponseEntity<>(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Lấy thông tin người dùng thành công",
                userService.getUserById(id)
        ), HttpStatus.OK);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
        return new ResponseEntity<>(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Cập nhật người dùng thành công",
                userService.updateUser(id, request)
        ), HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Xóa người dùng thành công",
                null
        ), HttpStatus.OK);
    }
}
