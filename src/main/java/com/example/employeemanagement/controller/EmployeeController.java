// EmployeeController.java
package com.example.employeemanagement.controller;

import com.example.employeemanagement.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@Tag(name = "Employee Management", description = "APIs for managing employees")
public class EmployeeController extends BaseController<EmployeeService, EmployeeDto> {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        super(employeeService);
        this.employeeService = employeeService;
    }

    @PostMapping
    @Operation(summary = "Create a new employee")
    public ResponseEntity<EmployeeDto> create(@Valid @RequestBody EmployeeRequest request) {
        return ResponseEntity.ok(employeeService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an employee")
    public ResponseEntity<EmployeeDto> update(@PathVariable Long id,
                                              @Valid @RequestBody EmployeeRequest request) {
        return ResponseEntity.ok(employeeService.update(id, request));
    }

    @PostMapping("/search")
    @Operation(summary = "Search employees with filters")
    public ResponseEntity<Page<EmployeeDto>> searchEmployees(@RequestBody EmployeeSearchRequest searchRequest) {
        return ResponseEntity.ok(employeeService.searchEmployees(searchRequest));
    }

    @PatchMapping("/{id}/photo")
    @Operation(summary = "Update employee photo")
    public ResponseEntity<EmployeeDto> updatePhoto(@PathVariable Long id,
                                                   @RequestParam String photoUrl) {
        return ResponseEntity.ok(employeeService.updatePhoto(id, photoUrl));
    }
}