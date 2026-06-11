package com.example.it211project.controller;

import com.example.it211project.model.dto.request.ChangePasswordRequest;
import com.example.it211project.model.dto.response.ApiResponse;
import com.example.it211project.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PatchMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(Authentication authentication,
                                                            @Valid @RequestBody ChangePasswordRequest request) {

        userService.changePassword(authentication.getName(), request);
        return new ResponseEntity<>(new ApiResponse<>(
                200,
                "Đổi mật khẩu thành công",
                null
        ), HttpStatus.OK);
    }
}
