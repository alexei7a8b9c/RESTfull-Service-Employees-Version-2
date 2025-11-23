package com.example.employeemanagement.dto.response;

import com.example.employeemanagement.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String username;
    private Role role;
    private String token;
}