package com.example.employeemanagement.service.impl;

import com.example.employeemanagement.dto.request.EmployeeFilterRequest;
import com.example.employeemanagement.dto.request.EmployeeRequest;
import com.example.employeemanagement.dto.response.EmployeeResponse;
import com.example.employeemanagement.entity.Department;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.exception.ResourceNotFoundException;
import com.example.employeemanagement.mapper.EmployeeMapper;
import com.example.employeemanagement.repository.DepartmentRepository;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.service.EmployeeService;
import com.example.employeemanagement.specification.EmployeeSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponse> findAll() {
        log.info("Fetching all employees");
        return employeeRepository.findByIsDeletedFalse(Pageable.unpaged())
                .stream()
                .map(employeeMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeResponse> findAll(EmployeeFilterRequest filterRequest) {
        log.info("Fetching employees with filters: {}", filterRequest);

        Specification<Employee> spec = Specification.where(EmployeeSpecification.notDeleted());

        if (filterRequest.getFirstName() != null) {
            spec = spec.and(EmployeeSpecification.hasFirstName(filterRequest.getFirstName()));
        }

        if (filterRequest.getLastName() != null) {
            spec = spec.and(EmployeeSpecification.hasLastName(filterRequest.getLastName()));
        }

        if (filterRequest.getStatus() != null) {
            spec = spec.and(EmployeeSpecification.hasStatus(filterRequest.getStatus()));
        }

        if (filterRequest.getManagerName() != null) {
            spec = spec.and(EmployeeSpecification.hasManagerName(filterRequest.getManagerName()));
        }

        if (filterRequest.getDepartmentIds() != null && !filterRequest.getDepartmentIds().isEmpty()) {
            spec = spec.and(EmployeeSpecification.hasDepartments(filterRequest.getDepartmentIds()));
        }

        Sort sort = Sort.by(filterRequest.getSortDirection(), filterRequest.getSortBy());
        Pageable pageable = PageRequest.of(filterRequest.getPage(), filterRequest.getSize(), sort);

        Page<Employee> employees = employeeRepository.findAll(spec, pageable);
        return employees.map(employeeMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponse findById(Long id) {
        log.info("Fetching employee with id: {}", id);
        Employee employee = employeeRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        return employeeMapper.toResponse(employee);
    }

    @Override
    @Transactional
    public EmployeeResponse create(EmployeeRequest employeeRequest) {
        log.info("Creating new employee: {}", employeeRequest);

        Employee employee = employeeMapper.toEntity(employeeRequest);

        // Set manager if provided
        if (employeeRequest.getManagerId() != null) {
            Employee manager = employeeRepository.findByIdAndIsDeletedFalse(employeeRequest.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Manager not found with id: " + employeeRequest.getManagerId()));
            employee.setManager(manager);
        }

        // Set departments if provided
        if (employeeRequest.getDepartmentIds() != null && !employeeRequest.getDepartmentIds().isEmpty()) {
            Set<Department> departments = new HashSet<>(
                    departmentRepository.findAllById(employeeRequest.getDepartmentIds()));
            employee.setDepartments(departments);
        }

        Employee savedEmployee = employeeRepository.save(employee);
        log.info("Employee created successfully with id: {}", savedEmployee.getId());

        return employeeMapper.toResponse(savedEmployee);
    }

    @Override
    @Transactional
    public EmployeeResponse update(Long id, EmployeeRequest employeeRequest) {
        log.info("Updating employee with id: {}", id);

        Employee existingEmployee = employeeRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        employeeMapper.updateEntityFromRequest(employeeRequest, existingEmployee);

        // Update manager if provided
        if (employeeRequest.getManagerId() != null) {
            Employee manager = employeeRepository.findByIdAndIsDeletedFalse(employeeRequest.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Manager not found with id: " + employeeRequest.getManagerId()));
            existingEmployee.setManager(manager);
        } else {
            existingEmployee.setManager(null);
        }

        // Update departments if provided
        if (employeeRequest.getDepartmentIds() != null) {
            Set<Department> departments = new HashSet<>(
                    departmentRepository.findAllById(employeeRequest.getDepartmentIds()));
            existingEmployee.setDepartments(departments);
        }

        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        log.info("Employee updated successfully with id: {}", updatedEmployee.getId());

        return employeeMapper.toResponse(updatedEmployee);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Soft deleting employee with id: {}", id);

        Employee employee = employeeRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        employee.setIsDeleted(true);
        employeeRepository.save(employee);

        log.info("Employee soft deleted successfully with id: {}", id);
    }

    @Override
    @Transactional
    public EmployeeResponse updatePhoto(Long id, String photoUrl) {
        log.info("Updating photo for employee with id: {}", id);

        Employee employee = employeeRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        employee.setPhotoUrl(photoUrl);
        Employee updatedEmployee = employeeRepository.save(employee);

        log.info("Photo updated successfully for employee with id: {}", id);
        return employeeMapper.toResponse(updatedEmployee);
    }
}