// BaseMapper.java
package com.example.employeemanagement.mapper;

public interface DepartmentMapper<T extends BaseEntity, D extends BaseDto> {

    D toDto(T entity);
    T toEntity(D dto);
}