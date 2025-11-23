// EmployeeRepository.java
package com.example.employeemanagement.repository;

import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.entity.EmployeeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends BaseRepository<Employee> {

    Optional<Employee> findByEmailAndDeletedFalse(String email);

    @Query("SELECT e FROM Employee e WHERE e.deleted = false AND " +
            "(LOWER(e.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
            "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :name, '%')))")
    Page<Employee> findByNameContaining(@Param("name") String name, Pageable pageable);

    Page<Employee> findByStatusInAndDeletedFalse(List<EmployeeStatus> statuses, Pageable pageable);

    @Query("SELECT e FROM Employee e JOIN e.departments d WHERE d.name IN :departmentNames AND e.deleted = false")
    Page<Employee> findByDepartmentNames(@Param("departmentNames") List<String> departmentNames, Pageable pageable);

    List<Employee> findByManagerIdAndDeletedFalse(Long managerId);
}