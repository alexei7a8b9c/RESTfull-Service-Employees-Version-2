package com.example.employeemanagement.service.impl;

import com.example.employeemanagement.dto.request.DepartmentRequest;
import com.example.employeemanagement.dto.response.DepartmentResponse;
import com.example.employeemanagement.entity.Department;
import com.example.employeemanagement.exception.ResourceNotFoundException;
import com.example.employeemanagement.mapper.DepartmentMapper;
import com.example.employeemanagement.repository.DepartmentRepository;
import com.example.employeemanagement.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponse> findAll() {
        log.info("Fetching all departments");
        return departmentRepository.findByIsDeletedFalse()
                .stream()
                .map(departmentMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponse findById(Long id) {
        log.info("Fetching department with id: {}", id);
        Department department = departmentRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
        return departmentMapper.toResponse(department);
    }

    @Override
    @Transactional
    public DepartmentResponse create(DepartmentRequest departmentRequest) {
        log.info("Creating new department: {}", departmentRequest);

        // Check if department with same name already exists
        departmentRepository.findByNameAndIsDeletedFalse(departmentRequest.getName())
                .ifPresent(dept -> {
                    throw new IllegalArgumentException("Department with name '" + departmentRequest.getName() + "' already exists");
                });

        Department department = departmentMapper.toEntity(departmentRequest);
        Department savedDepartment = departmentRepository.save(department);

        log.info("Department created successfully with id: {}", savedDepartment.getId());
        return departmentMapper.toResponse(savedDepartment);
    }

    @Override
    @Transactional
    public DepartmentResponse update(Long id, DepartmentRequest departmentRequest) {
        log.info("Updating department with id: {}", id);

        Department existingDepartment = departmentRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));

        // Check if another department with same name already exists
        if (!existingDepartment.getName().equals(departmentRequest.getName())) {
            departmentRepository.findByNameAndIsDeletedFalse(departmentRequest.getName())
                    .ifPresent(dept -> {
                        throw new IllegalArgumentException("Department with name '" + departmentRequest.getName() + "' already exists");
                    });
        }

        departmentMapper.updateEntityFromRequest(departmentRequest, existingDepartment);
        Department updatedDepartment = departmentRepository.save(existingDepartment);

        log.info("Department updated successfully with id: {}", updatedDepartment.getId());
        return departmentMapper.toResponse(updatedDepartment);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Soft deleting department with id: {}", id);

        Department department = departmentRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));

        department.setIsDeleted(true);
        departmentRepository.save(department);

        log.info("Department soft deleted successfully with id: {}", id);
    }
}