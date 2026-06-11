package com.example.it211project.service.impl;

import com.example.it211project.exception.UnauthorizedException;
import com.example.it211project.model.dto.request.LoginRequest;
import com.example.it211project.model.dto.request.RefreshTokenRequest;
import com.example.it211project.model.dto.response.LoginResponse;
import com.example.it211project.model.entity.TokenBlacklist;
import com.example.it211project.model.entity.User;
import com.example.it211project.repository.TokenBlacklistRepository;
import com.example.it211project.repository.UserRepository;
import com.example.it211project.security.jwt.JwtService;
import com.example.it211project.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenBlacklistRepository tokenBlacklistRepository;

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new UnauthorizedException("Tên người dùng hoặc mật khẩu không hợp lệ"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Tên người dùng hoặc mật khẩu không hợp lệ");
        }

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new LoginResponse(
                accessToken,
                refreshToken,
                user.getUsername(),
                user.getRole().name()
        );
    }

    @Override
    public LoginResponse refreshToken(RefreshTokenRequest request) {
        try {
            String username = jwtService.extractUsername(request.getRefreshToken());
            User user = userRepository.findByUsername(username).orElseThrow(() -> new UnauthorizedException("Refresh token không hợp lệ"));

            return new LoginResponse(
                    jwtService.generateAccessToken(user),
                    jwtService.generateRefreshToken(user),
                    user.getUsername(),
                    user.getRole().name());
        } catch (Exception e) {
            throw new UnauthorizedException("Refresh token không hợp lệ");
        }
    }

    @Override
    public void logout(String token) {
        TokenBlacklist blacklist = new TokenBlacklist();

        blacklist.setToken(token);
        blacklist.setExpiredAt(jwtService.extractExpiration(token)
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime());

        tokenBlacklistRepository.save(blacklist);
    }
}
