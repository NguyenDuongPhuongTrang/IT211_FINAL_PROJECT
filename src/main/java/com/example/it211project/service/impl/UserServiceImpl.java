package com.example.it211project.service.impl;

import com.example.it211project.enums.Role;
import com.example.it211project.exception.ConflictException;
import com.example.it211project.exception.ResourceNotFoundException;
import com.example.it211project.exception.UnauthorizedException;
import com.example.it211project.model.dto.request.ChangePasswordRequest;
import com.example.it211project.model.dto.request.ForgotPasswordRequest;
import com.example.it211project.model.dto.request.RegisterRequest;
import com.example.it211project.model.dto.request.UserRequest;
import com.example.it211project.model.dto.response.UserResponse;
import com.example.it211project.model.entity.User;
import com.example.it211project.repository.UserRepository;
import com.example.it211project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse register(RegisterRequest request) {
        if(userRepository.existsByUsername(request.getUsername())){
            throw new ConflictException("Username đã tồn tại");
        }

        if(userRepository.existsByEmail(request.getEmail())){
            throw new ConflictException("Email đã tồn tại");
        }

        User user = new User();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.ROLE_CUSTOMER);
        user.setActive(true);

        userRepository.save(user);

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .active(user.getActive())
                .build();
    }

    @Override
    public Page<UserResponse> getUsers(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<User> users = userRepository.findByUsernameContainingIgnoreCase(keyword, pageable);

        List<UserResponse> responses =
                users.getContent()
                        .stream()
                        .map(this::toResponse)
                        .toList();

        return new PageImpl<>(responses, pageable, users.getTotalElements());
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy User"));
        return toResponse(user);
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy User"));

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setActive(request.getActive());

        userRepository.save(user);

        return toResponse(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy User"));
        userRepository.delete(user);
    }

    @Override
    public void changePassword(String username, ChangePasswordRequest request) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new UnauthorizedException("Mật khẩu cũ không chính xác");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public User getUserEntityByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));

        if (!user.getEmail().equals(request.getEmail())) {
            throw new UnauthorizedException("Email không khớp với tài khoản");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .active(user.getActive())
                .build();
    }
}
