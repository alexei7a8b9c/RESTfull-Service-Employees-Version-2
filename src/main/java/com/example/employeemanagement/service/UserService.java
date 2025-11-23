package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.request.AuthRequest;
import com.example.employeemanagement.dto.response.AuthResponse;

public interface UserService {
    AuthResponse authenticate(AuthRequest authRequest);
    void initializeDefaultUsers();
}