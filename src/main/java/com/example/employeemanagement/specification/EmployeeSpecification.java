package com.example.employeemanagement.specification;

import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.enums.EmployeeStatus;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.JoinType;
import java.util.Set;

public class EmployeeSpecification {

    public static Specification<Employee> notDeleted() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isDeleted"), false);
    }

    public static Specification<Employee> hasFirstName(String firstName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("firstName")),
                        "%" + firstName.toLowerCase() + "%"
                );
    }

    public static Specification<Employee> hasLastName(String lastName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("lastName")),
                        "%" + lastName.toLowerCase() + "%"
                );
    }

    public static Specification<Employee> hasStatus(EmployeeStatus status) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Employee> hasManagerName(String managerName) {
        return (root, query, criteriaBuilder) -> {
            var managerJoin = root.join("manager", JoinType.LEFT);
            return criteriaBuilder.or(
                    criteriaBuilder.like(
                            criteriaBuilder.lower(managerJoin.get("firstName")),
                            "%" + managerName.toLowerCase() + "%"
                    ),
                    criteriaBuilder.like(
                            criteriaBuilder.lower(managerJoin.get("lastName")),
                            "%" + managerName.toLowerCase() + "%"
                    )
            );
        };
    }

    public static Specification<Employee> hasDepartments(Set<Long> departmentIds) {
        return (root, query, criteriaBuilder) -> {
            var departmentsJoin = root.join("departments", JoinType.INNER);
            return departmentsJoin.get("id").in(departmentIds);
        };
    }
}