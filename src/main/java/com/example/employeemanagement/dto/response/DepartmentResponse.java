package com.example.employeemanagement.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DepartmentResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}