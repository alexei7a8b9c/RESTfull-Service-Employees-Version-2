package com.example.employeemanagement.dto.request;

import com.example.employeemanagement.enums.EmployeeStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class EmployeeRequest {
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Email(message = "Invalid email format")
    private String email;

    private String phone;

    @NotNull(message = "Status is required")
    private EmployeeStatus status;

    private String photoUrl;

    private LocalDate hireDate;

    private Long managerId;

    private Set<Long> departmentIds;
}