// DepartmentService.java
package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.department.DepartmentDto;
import com.example.employeemanagement.dto.department.DepartmentRequest;
import com.example.employeemanagement.entity.Department;

public interface DepartmentService extends BaseService<Department, DepartmentDto> {

    DepartmentDto create(DepartmentRequest request);
    DepartmentDto update(Long id, DepartmentRequest request);
    boolean existsByName(String name);
}