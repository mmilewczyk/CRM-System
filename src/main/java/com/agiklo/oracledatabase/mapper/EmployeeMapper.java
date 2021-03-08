package com.agiklo.oracledatabase.mapper;

import com.agiklo.oracledatabase.entity.Employee;
import com.agiklo.oracledatabase.entity.dto.EmployeeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(target = "role", source = "userRole")
    @Mapping(target = "departmentId", source = "department.id")
    @Mapping(target = "departmentName", source = "department.departmentName")
    EmployeeDTO mapEmployeeToDto(Employee employee);
}
