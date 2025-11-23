// EmployeeMapper.java
package com.example.employeemanagement.mapper;

import com.example.employeemanagement.entity.Employee;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {DepartmentMapper.class})
public interface EmployeeMapper extends DepartmentMapper<Employee, EmployeeDto> {

    @Mapping(target = "managerId", source = "manager.id")
    @Mapping(target = "managerName", expression = "java(entity.getManager() != null ? entity.getManager().getFullName() : null)")
    EmployeeDto toDto(Employee entity);

    @Mapping(target = "manager", ignore = true)
    @Mapping(target = "departments", ignore = true)
    Employee toEntity(EmployeeDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "manager", ignore = true)
    @Mapping(target = "departments", ignore = true)
    Employee toEntity(EmployeeRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "manager", ignore = true)
    @Mapping(target = "departments", ignore = true)
    void updateEntityFromRequest(EmployeeRequest request, @MappingTarget Employee entity);
}