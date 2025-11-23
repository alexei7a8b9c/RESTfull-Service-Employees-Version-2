package com.example.employeemanagement.dto.response;

import com.example.employeemanagement.enums.EmployeeStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class EmployeeResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String phone;
    private EmployeeStatus status;
    private String photoUrl;
    private LocalDate hireDate;
    private Long managerId;
    private String managerName;
    private Set<DepartmentResponse> departments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}