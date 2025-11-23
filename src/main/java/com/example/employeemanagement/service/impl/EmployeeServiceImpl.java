// EmployeeServiceImpl.java
package com.example.employeemanagement.service.impl;

import com.example.employeemanagement.dto.employee.EmployeeDto;
import com.example.employeemanagement.dto.employee.EmployeeRequest;
import com.example.employeemanagement.dto.employee.EmployeeSearchRequest;
import com.example.employeemanagement.entity.Department;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.exception.DuplicateResourceException;
import com.example.employeemanagement.exception.ResourceNotFoundException;
import com.example.employeemanagement.mapper.EmployeeMapper;
import com.example.employeemanagement.repository.DepartmentRepository;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.repository.specification.EmployeeSpecifications;
import com.example.employeemanagement.service.EmployeeService;
import lombok.RequiredArgsConstructor;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl extends AbstractBaseService<Employee, EmployeeDto> implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               DepartmentRepository departmentRepository,
                               EmployeeMapper employeeMapper) {
        super(employeeRepository, employeeMapper);
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.employeeMapper = employeeMapper;
    }

    @Override
    @Transactional
    public EmployeeDto create(EmployeeRequest request) {
        // Проверка уникальности email
        if (request.getEmail() != null && !request.getEmail().isEmpty() &&
                employeeRepository.findByEmailAndDeletedFalse(request.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Employee with email '" + request.getEmail() + "' already exists");
        }

        Employee employee = employeeMapper.toEntity(request);

        // Установка менеджера
        if (request.getManagerId() != null) {
            Employee manager = employeeRepository.findByIdAndDeletedFalse(request.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Manager not found with id: " + request.getManagerId()));
            employee.setManager(manager);
        }

        // Установка отделов
        if (request.getDepartmentIds() != null && !request.getDepartmentIds().isEmpty()) {
            Set<Department> departments = new HashSet<>(
                    departmentRepository.findAllById(request.getDepartmentIds()));

            // Проверяем, что все отделы существуют
            if (departments.size() != request.getDepartmentIds().size()) {
                throw new ResourceNotFoundException("One or more departments not found");
            }

            employee.setDepartments(departments);
        }

        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.toDto(savedEmployee);
    }

    @Override
    @Transactional
    public EmployeeDto update(Long id, EmployeeRequest request) {
        Employee existingEmployee = employeeRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        // Проверка уникальности email (если изменился)
        if (request.getEmail() != null && !request.getEmail().isEmpty() &&
                !request.getEmail().equals(existingEmployee.getEmail()) &&
                employeeRepository.findByEmailAndDeletedFalse(request.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Employee with email '" + request.getEmail() + "' already exists");
        }

        employeeMapper.updateEntityFromRequest(request, existingEmployee);

        // Обновление менеджера
        if (request.getManagerId() != null) {
            Employee manager = employeeRepository.findByIdAndDeletedFalse(request.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Manager not found with id: " + request.getManagerId()));
            existingEmployee.setManager(manager);
        } else {
            existingEmployee.setManager(null);
        }

        // Обновление отделов
        if (request.getDepartmentIds() != null) {
            Set<Department> departments = new HashSet<>(
                    departmentRepository.findAllById(request.getDepartmentIds()));

            // Проверяем, что все отделы существуют
            if (departments.size() != request.getDepartmentIds().size()) {
                throw new ResourceNotFoundException("One or more departments not found");
            }

            existingEmployee.setDepartments(departments);
        }

        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        return employeeMapper.toDto(updatedEmployee);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeDto> searchEmployees(EmployeeSearchRequest searchRequest) {
        Specification<Employee> spec = Specification.where(EmployeeSpecifications.notDeleted())
                .and(EmployeeSpecifications.hasName(searchRequest.getName()))
                .and(EmployeeSpecifications.hasStatuses(searchRequest.getStatuses()))
                .and(EmployeeSpecifications.hasManagerName(searchRequest.getManagerName()))
                .and(EmployeeSpecifications.hasDepartments(searchRequest.getDepartmentNames()));

        Sort sort = Sort.by(searchRequest.getSortDirection(), searchRequest.getSortBy());
        Pageable pageable = PageRequest.of(
                searchRequest.getPage() != null ? searchRequest.getPage() : 0,
                searchRequest.getSize() != null ? searchRequest.getSize() : 20,
                sort
        );

        return employeeRepository.findAll(spec, pageable)
                .map(employeeMapper::toDto);
    }

    @Override
    @Transactional
    public EmployeeDto updatePhoto(Long id, String photoUrl) {
        Employee employee = employeeRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        employee.setPhotoUrl(photoUrl);
        Employee updatedEmployee = employeeRepository.save(employee);
        return employeeMapper.toDto(updatedEmployee);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return employeeRepository.findByEmailAndDeletedFalse(email).isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDto> findByManagerId(Long managerId) {
        return employeeRepository.findByManagerIdAndDeletedFalse(managerId).stream()
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDto> findAll() {
        return employeeRepository.findAllByDeletedFalse().stream()
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDto findById(Long id) {
        Employee employee = employeeRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        return employeeMapper.toDto(employee);
    }
}