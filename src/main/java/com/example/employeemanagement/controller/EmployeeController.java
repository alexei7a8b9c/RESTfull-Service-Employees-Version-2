package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.request.EmployeeFilterRequest;
import com.example.employeemanagement.dto.request.EmployeeRequest;
import com.example.employeemanagement.dto.response.ApiResponse;
import com.example.employeemanagement.dto.response.EmployeeResponse;
import com.example.employeemanagement.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@Tag(name = "Employee Management", description = "APIs for managing employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    @Operation(summary = "Get all employees")
    public ResponseEntity<ApiResponse<List<EmployeeResponse>>> getAllEmployees() {
        List<EmployeeResponse> employees = employeeService.findAll();
        return ResponseEntity.ok(ApiResponse.success(employees));
    }

    @PostMapping("/search")
    @Operation(summary = "Search employees with filters")
    public ResponseEntity<ApiResponse<Page<EmployeeResponse>>> searchEmployees(
            @RequestBody EmployeeFilterRequest filterRequest) {
        Page<EmployeeResponse> employees = employeeService.findAll(filterRequest);
        return ResponseEntity.ok(ApiResponse.success(employees));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get employee by ID")
    public ResponseEntity<ApiResponse<EmployeeResponse>> getEmployeeById(@PathVariable Long id) {
        EmployeeResponse employee = employeeService.findById(id);
        return ResponseEntity.ok(ApiResponse.success(employee));
    }

    @PostMapping
    @Operation(summary = "Create new employee")
    public ResponseEntity<ApiResponse<EmployeeResponse>> createEmployee(
            @Valid @RequestBody EmployeeRequest employeeRequest) {
        EmployeeResponse employee = employeeService.create(employeeRequest);
        return ResponseEntity.ok(ApiResponse.success("Employee created successfully", employee));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update employee")
    public ResponseEntity<ApiResponse<EmployeeResponse>> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequest employeeRequest) {
        EmployeeResponse employee = employeeService.update(id, employeeRequest);
        return ResponseEntity.ok(ApiResponse.success("Employee updated successfully", employee));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete employee")
    public ResponseEntity<ApiResponse<Void>> deleteEmployee(@PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Employee deleted successfully", null));
    }

    @PatchMapping("/{id}/photo")
    @Operation(summary = "Update employee photo")
    public ResponseEntity<ApiResponse<EmployeeResponse>> updateEmployeePhoto(
            @PathVariable Long id,
            @RequestParam String photoUrl) {
        EmployeeResponse employee = employeeService.updatePhoto(id, photoUrl);
        return ResponseEntity.ok(ApiResponse.success("Photo updated successfully", employee));
    }
}