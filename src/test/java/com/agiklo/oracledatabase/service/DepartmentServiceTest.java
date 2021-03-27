package com.agiklo.oracledatabase.service;

import com.agiklo.oracledatabase.entity.Departments;
import com.agiklo.oracledatabase.entity.dto.DepartmentDTO;
import com.agiklo.oracledatabase.repository.DepartmentsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("dev")
class DepartmentServiceTest {

    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private DepartmentsRepository departmentsRepository;

    @Test
    @Transactional
    void shouldGetDepartmentById() {
        Departments departments = prepareDepartmentsToTest();
        departmentsRepository.save(departments);
        //when
        DepartmentDTO departmentDTO = departmentService.getDepartmentById(departments.getId());
        //then
        assertThat(departmentDTO).isNotNull();
        assertThat(departmentDTO.getCity()).isEqualTo("Gdynia");
        assertThat(departmentDTO.getDepartmentName()).isEqualTo("Sprzedaz");
    }

    Departments prepareDepartmentsToTest(){
        Departments departments = new Departments();
        departments.setCity("Gdynia");
        departments.setDepartmentName("Sprzedaz");
        return departments;
    }
}
