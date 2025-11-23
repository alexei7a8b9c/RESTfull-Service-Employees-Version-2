package com.example.employeemanagement.service.impl;

import com.example.employeemanagement.config.JwtUtil;
import com.example.employeemanagement.dto.request.AuthRequest;
import com.example.employeemanagement.dto.response.AuthResponse;
import com.example.employeemanagement.entity.User;
import com.example.employeemanagement.enums.Role;
import com.example.employeemanagement.exception.ResourceNotFoundException;
import com.example.employeemanagement.repository.UserRepository;
import com.example.employeemanagement.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public AuthResponse authenticate(AuthRequest authRequest) {
        log.info("Authenticating user: {}", authRequest.getUsername());

        User user = userRepository.findByUsername(authRequest.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Простая проверка пароля (без шифрования для тестирования)
        if (!user.getPassword().equals(authRequest.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(user);

        log.info("User {} authenticated successfully", authRequest.getUsername());
        return new AuthResponse(user.getUsername(), user.getRole(), token);
    }

    @Override
    @PostConstruct
    public void initializeDefaultUsers() {
        try {
            if (userRepository.count() == 0) {
                log.info("Initializing default users");

                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword("admin123"); // Простой пароль
                admin.setRole(Role.ADMIN);
                userRepository.save(admin);

                User user = new User();
                user.setUsername("user");
                user.setPassword("user123"); // Простой пароль
                user.setRole(Role.USER);
                userRepository.save(user);

                log.info("Default users initialized successfully");
            }
        } catch (Exception e) {
            log.error("Error initializing default users", e);
        }
    }
}