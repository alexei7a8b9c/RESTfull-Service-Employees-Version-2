// DepartmentRepository.java
package com.example.employeemanagement.repository;

import com.example.employeemanagement.entity.Department;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends BaseRepository<Department> {

    Optional<Department> findByNameAndDeletedFalse(String name);
    boolean existsByNameAndDeletedFalse(String name);
}