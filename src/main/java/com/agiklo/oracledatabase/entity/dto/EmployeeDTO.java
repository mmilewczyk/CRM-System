package com.agiklo.oracledatabase.entity.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String role;
    private String email;
    private Double salary;
    private Long departmentId;
    private String departmentName;
}
