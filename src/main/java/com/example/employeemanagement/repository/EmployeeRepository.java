package com.example.employeemanagement.repository;

import com.example.employeemanagement.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

    Page<Employee> findByIsDeletedFalse(Pageable pageable);

    Optional<Employee> findByIdAndIsDeletedFalse(Long id);

    @Query("SELECT e FROM Employee e WHERE " +
            "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
            "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :name, '%')) AND e.isDeleted = false")
    List<Employee> findByNameContainingIgnoreCase(@Param("name") String name);

    List<Employee> findByStatusAndIsDeletedFalse(com.example.employeemanagement.enums.EmployeeStatus status);

    @Query("SELECT e FROM Employee e JOIN e.departments d WHERE d.id IN :departmentIds AND e.isDeleted = false")
    List<Employee> findByDepartmentIds(@Param("departmentIds") List<Long> departmentIds);
}