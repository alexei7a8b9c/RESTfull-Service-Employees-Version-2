package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.request.DepartmentRequest;
import com.example.employeemanagement.dto.response.DepartmentResponse;

import java.util.List;

public interface DepartmentService {
    List<DepartmentResponse> findAll();
    DepartmentResponse findById(Long id);
    DepartmentResponse create(DepartmentRequest departmentRequest);
    DepartmentResponse update(Long id, DepartmentRequest departmentRequest);
    void delete(Long id);
}