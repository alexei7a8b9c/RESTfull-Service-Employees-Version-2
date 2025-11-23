// EmployeeService.java
package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.employee.EmployeeDto;
import com.example.employeemanagement.dto.employee.EmployeeRequest;
import com.example.employeemanagement.dto.employee.EmployeeSearchRequest;
import com.example.employeemanagement.entity.Employee;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmployeeService extends BaseService<Employee, EmployeeDto> {

    EmployeeDto create(EmployeeRequest request);
    EmployeeDto update(Long id, EmployeeRequest request);
    Page<EmployeeDto> searchEmployees(EmployeeSearchRequest searchRequest);
    EmployeeDto updatePhoto(Long id, String photoUrl);
    boolean existsByEmail(String email);
    List<EmployeeDto> findByManagerId(Long managerId);
}