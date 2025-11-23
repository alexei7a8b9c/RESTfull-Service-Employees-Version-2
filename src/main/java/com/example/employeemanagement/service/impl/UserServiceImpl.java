package com.example.employeemanagement.service.impl;

import com.example.employeemanagement.dto.request.AuthRequest;
import com.example.employeemanagement.dto.response.AuthResponse;
import com.example.employeemanagement.entity.User;
import com.example.employeemanagement.enums.Role;
import com.example.employeemanagement.repository.UserRepository;
import com.example.employeemanagement.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse authenticate(AuthRequest authRequest) {
        // Simplified authentication - in real application use JWT tokens
        User user = userRepository.findByUsername(authRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return new AuthResponse(user.getUsername(), user.getRole(), "dummy-token");
    }

    @Override
    @PostConstruct
    public void initializeDefaultUsers() {
        if (userRepository.count() == 0) {
            log.info("Initializing default users");

            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);

            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setRole(Role.USER);
            userRepository.save(user);

            log.info("Default users initialized successfully");
        }
    }
}