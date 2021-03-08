package com.agiklo.oracledatabase.mapper;

import com.agiklo.oracledatabase.entity.Departments;
import com.agiklo.oracledatabase.entity.dto.DepartmentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    @Mapping(target = "departmentId", source = "id")
    @Mapping(target = "departmentName", source = "departmentName")
    @Mapping(target = "managerFirstName", source = "managers.firstName")
    @Mapping(target = "managerLastName", source = "managers.lastName")
    DepartmentDTO mapDepartmentToDto(Departments departments);

}
