package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.request.EmployeeFilterRequest;
import com.example.employeemanagement.dto.request.EmployeeRequest;
import com.example.employeemanagement.dto.response.EmployeeResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmployeeService {
    List<EmployeeResponse> findAll();
    Page<EmployeeResponse> findAll(EmployeeFilterRequest filterRequest);
    EmployeeResponse findById(Long id);
    EmployeeResponse create(EmployeeRequest employeeRequest);
    EmployeeResponse update(Long id, EmployeeRequest employeeRequest);
    void delete(Long id);
    EmployeeResponse updatePhoto(Long id, String photoUrl);
}