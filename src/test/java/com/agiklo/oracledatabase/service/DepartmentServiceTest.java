package com.agiklo.oracledatabase.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.agiklo.oracledatabase.entity.Departments;
import com.agiklo.oracledatabase.entity.Employee;
import com.agiklo.oracledatabase.entity.dto.DepartmentDTO;
import com.agiklo.oracledatabase.enums.USER_ROLE;
import com.agiklo.oracledatabase.mapper.DepartmentMapper;
import com.agiklo.oracledatabase.repository.DepartmentsRepository;

import java.io.IOException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

@ContextConfiguration(classes = {DepartmentService.class})
@ExtendWith(SpringExtension.class)
@Execution(ExecutionMode.CONCURRENT)
class DepartmentServiceTest {

    @MockBean
    private DepartmentMapper departmentMapper;

    @Autowired
    private DepartmentService departmentService;

    @MockBean
    private DepartmentsRepository departmentsRepository;

    @Test
    void shouldReturnEmptyListOfDepartments() {
        // when
        when(this.departmentsRepository.findAllBy(any())).thenReturn(new ArrayList<>());

        // then
        assertTrue(this.departmentService.getAllDepartments(null).isEmpty());
        verify(this.departmentsRepository).findAllBy(any());
    }

    @Test
    void shouldGetAllDepartments() {
        // given
        Departments department = createDepartment1();
        ArrayList<Departments> departmentsList = new ArrayList<>();
        departmentsList.add(department);

        // when
        when(this.departmentsRepository.findAllBy(any())).thenReturn(departmentsList);
        when(this.departmentMapper.mapDepartmentToDto(any())).thenReturn(new DepartmentDTO());

        // then
        assertEquals(1, this.departmentService.getAllDepartments(null).size());
        verify(this.departmentMapper).mapDepartmentToDto(any());
        verify(this.departmentsRepository).findAllBy(any());
    }

    @Test
    void shouldGetDepartmentById() {
        // given
        Departments department = createDepartment1();
        DepartmentDTO departmentDTO = new DepartmentDTO();
        Optional<Departments> ofResult = Optional.of(department);

        // when
        when(this.departmentsRepository.findById(any())).thenReturn(ofResult);
        when(this.departmentMapper.mapDepartmentToDto(any())).thenReturn(departmentDTO);

        // then
        assertSame(departmentDTO, this.departmentService.getDepartmentById(123L));
        verify(this.departmentMapper).mapDepartmentToDto(any());
        verify(this.departmentsRepository).findById(any());
    }

    @Test
    void shouldNotGetDepartmentById() {
        // given
        // when
        when(this.departmentsRepository.findById(any())).thenReturn(Optional.empty());
        when(this.departmentMapper.mapDepartmentToDto(any())).thenReturn(new DepartmentDTO());

        // then
        assertThrows(ResponseStatusException.class, () -> this.departmentService.getDepartmentById(123L));
        verify(this.departmentsRepository).findById(any());
    }

    @Test
    void shouldAddNewDepartment() {
        // given
        Employee employee = createEmployee1();
        Departments department = new Departments("IT", employee, "Gdansk");

        // when
        when(this.departmentsRepository.save(any())).thenReturn(department);

        // then
        assertSame(department, this.departmentService.addNewDepartment(new Departments()));
        verify(this.departmentsRepository).save(any());
    }

    @Test
    void shouldDeleteDepartmentById() {
        // when
        doNothing().when(this.departmentsRepository).deleteById(any());
        this.departmentService.deleteDepartmentById(123L);

        // then
        verify(this.departmentsRepository).deleteById(any());
    }

    @Test
    void shouldNotDeleteDepartmentByIdAndThrowException() {
        // when
        // then
        doThrow(new EmptyResultDataAccessException(3)).when(this.departmentsRepository).deleteById(any());
        assertThrows(ResponseStatusException.class, () -> this.departmentService.deleteDepartmentById(123L));

        verify(this.departmentsRepository).deleteById(any());
    }

    @Test
    void shouldExportToExcel() throws IOException {
        when(this.departmentsRepository.findAll()).thenReturn(new ArrayList<>());
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        this.departmentService.exportToExcel(mockHttpServletResponse);
        verify(this.departmentsRepository).findAll();
        assertTrue(mockHttpServletResponse.isCommitted());
        assertNull(mockHttpServletResponse.getRedirectedUrl());
        assertEquals("application/vnd.ms-excel", mockHttpServletResponse.getContentType());
    }

    @Test
    void shouldExportToPdf() throws IOException {
        when(this.departmentsRepository.findAll()).thenReturn(new ArrayList<>());
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        this.departmentService.exportToPDF(mockHttpServletResponse);
        verify(this.departmentsRepository).findAll();
        assertTrue(mockHttpServletResponse.isCommitted());
        assertNull(mockHttpServletResponse.getRedirectedUrl());
        assertEquals("application/pdf", mockHttpServletResponse.getContentType());
    }

    @Test
    void shouldGetZeroDepartmentsByName() {
        // when
        when(this.departmentsRepository.getDepartmentsByDepartmentNameContainingIgnoreCase(anyString(), any()))
                .thenReturn(new ArrayList<>());

        // then
        assertTrue(this.departmentService.getAllDepartmentsByName("IT", null).isEmpty());
        verify(this.departmentsRepository).getDepartmentsByDepartmentNameContainingIgnoreCase(anyString(), any());
    }

    @Test
    void shouldGetAllDepartmentsByName() {
        // given
        Departments department = createDepartment();
        ArrayList<Departments> departmentsList = new ArrayList<>();
        departmentsList.add(department);

        // when
        when(this.departmentsRepository.getDepartmentsByDepartmentNameContainingIgnoreCase(anyString(), any())).thenReturn(departmentsList);
        when(this.departmentMapper.mapDepartmentToDto(any())).thenReturn(new DepartmentDTO());

        // then
        assertEquals(1, this.departmentService.getAllDepartmentsByName("IT", null).size());

        verify(this.departmentMapper).mapDepartmentToDto(any());
        verify(this.departmentsRepository).getDepartmentsByDepartmentNameContainingIgnoreCase(anyString(), any());
    }

    private Employee createEmployee(){
        Employee employee = new Employee(
                "Mateusz",
                "Milewczyk",
                "mateusz@agiklocrm.com",
                "test", USER_ROLE.EMPLOYEE,
                "Pesel",
                "Male",
                LocalDate.ofEpochDay(1L),
                10000.0,
                new Departments());
        employee.setIsEnabled(true);
        employee.setIsLocked(true);
        return employee;
    }

    private Departments createDepartment(){
        return new Departments("IT", createEmployee(), "Gdansk");
    }

    private Employee createEmployee1(){
        Employee employee = new Employee(
                "Mateusz",
                "Milewczyk",
                "mateusz@agiklocrm.com",
                "test", USER_ROLE.EMPLOYEE,
                "Pesel",
                "Male",
                LocalDate.ofEpochDay(1L),
                10000.0,
                createDepartment());
        employee.setIsEnabled(true);
        employee.setIsLocked(true);
        return employee;
    }

    private Departments createDepartment1(){
        return new Departments("IT", createEmployee1(), "Gdansk");
    }
}

