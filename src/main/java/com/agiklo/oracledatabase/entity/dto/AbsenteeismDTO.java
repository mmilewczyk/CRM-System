package com.agiklo.oracledatabase.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AbsenteeismDTO {

    //source = "employee.id"
    private String employeeId;
    //source = "employee.firstName"
    private String firstName;
    //source = "employee.lastName"
    private String lastName;
    //source = "employee.department.departmentName"
    private String departmentName;
    //source = "reasonOfAbsenteeismCode.absenteeismName"
    private String absenteeismName;
    private Date dateFrom;
    private Date dateTo;
}
