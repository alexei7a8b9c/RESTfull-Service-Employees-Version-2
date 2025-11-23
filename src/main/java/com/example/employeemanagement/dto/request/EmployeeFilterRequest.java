package com.example.employeemanagement.dto.request;

import com.example.employeemanagement.enums.EmployeeStatus;
import lombok.Data;
import org.springframework.data.domain.Sort;

import java.util.Set;

@Data
public class EmployeeFilterRequest {
    private String firstName;
    private String lastName;
    private EmployeeStatus status;
    private String managerName;
    private Set<Long> departmentIds;
    private Integer page = 0;
    private Integer size = 20;
    private String sortBy = "id";
    private Sort.Direction sortDirection = Sort.Direction.ASC;
}