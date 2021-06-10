package com.agiklo.oracledatabase.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.agiklo.oracledatabase.entity.Absenteeism;
import com.agiklo.oracledatabase.entity.Departments;
import com.agiklo.oracledatabase.entity.Employee;
import com.agiklo.oracledatabase.entity.ReasonsOfAbsenteeism;
import com.agiklo.oracledatabase.entity.dto.AbsenteeismDTO;
import com.agiklo.oracledatabase.enums.USER_ROLE;
import com.agiklo.oracledatabase.mapper.AbsenteeismMapper;
import com.agiklo.oracledatabase.repository.AbsenteeismRepository;

import java.io.IOException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

@ContextConfiguration(classes = {AbsenteeismService.class})
@ExtendWith(SpringExtension.class)
@Execution(ExecutionMode.CONCURRENT)
class AbsenteeismServiceTest {

    @MockBean
    private AbsenteeismMapper absenteeismMapper;

    @MockBean
    private AbsenteeismRepository absenteeismRepository;

    @Autowired
    private AbsenteeismService absenteeismService;

    @Test
    void shouldGetAllAbsenteeismsAndReturnEmptyList() {
        when(this.absenteeismRepository.findAllBy(any())).thenReturn(new ArrayList<>());
        assertTrue(this.absenteeismService.getAllAbsenteeisms(null).isEmpty());
        verify(this.absenteeismRepository).findAllBy(any());
    }

    @Test
    void shouldGetAllAbsenteeisms() {
        when(this.absenteeismRepository.findAllBy(any()))
                .thenReturn(new ArrayList<>());
        assertTrue(this.absenteeismService.getAllAbsenteeisms(null).isEmpty());
        verify(this.absenteeismRepository).findAllBy(any());
    }

    @Test
    void shouldNotGetAbsenteeismByIdAndThrowsException(){
        when(this.absenteeismRepository.findById(any())).thenThrow(new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Absenteeism cannot be found, the specified id does not exist"));
        assertThrows(ResponseStatusException.class, () -> this.absenteeismService.getAbsenteeismById(any()));
        verify(this.absenteeismRepository).findById(any());
    }

    @Test
    void shouldGetAbsenteeismById() {
        Departments department = new Departments("IT", new Employee(),"Warsaw");

        Employee employee = new Employee(
                "Mateusz",
                "Milewczyk",
                "mateusz@agiklocrm.com",
                "test",
                USER_ROLE.EMPLOYEE,
                "Pesel",
                "Male",
                LocalDate.ofEpochDay(1L),
                10_000.00,
                department);
        employee.setIsEnabled(true);
        employee.setIsLocked(true);

        ReasonsOfAbsenteeism reasonsOfAbsenteeism = new ReasonsOfAbsenteeism(
                "Absenteeism Name", 'A', "Comments");

        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();

        Absenteeism absenteeism = new Absenteeism(
                employee,
                reasonsOfAbsenteeism,
                Date.from(atStartOfDayResult.atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(atStartOfDayResult.atZone(ZoneId.systemDefault()).toInstant()));

        when(this.absenteeismRepository.findById(any())).thenReturn(java.util.Optional.of(absenteeism));
        when(this.absenteeismMapper.mapAbsenteeismToDto(any()))
                .thenReturn(new AbsenteeismDTO("123", "Mateusz", "Milewczyk", "IT", "Absenteeism Name", Date.from(atStartOfDayResult.atZone(ZoneId.systemDefault()).toInstant()),
                        Date.from(atStartOfDayResult.atZone(ZoneId.systemDefault()).toInstant())));

        assertEquals("Absenteeism Name", this.absenteeismService.getAbsenteeismById(anyLong()).getAbsenteeismName());
        verify(this.absenteeismRepository).findById(anyLong());
        verify(this.absenteeismMapper).mapAbsenteeismToDto(absenteeism);
    }

    @Test
    void shouldAddNewAbsenteeism() {
        Departments department = new Departments("IT", new Employee(),"Warsaw");

        Employee employee = new Employee(
                "Mateusz",
                "Milewczyk",
                "mateusz@agiklocrm.com",
                "test",
                USER_ROLE.EMPLOYEE,
                "Pesel",
                "Male",
                LocalDate.ofEpochDay(1L),
                10_000.00,
                department);
        employee.setIsEnabled(true);
        employee.setIsLocked(true);

        ReasonsOfAbsenteeism reasonsOfAbsenteeism = new ReasonsOfAbsenteeism(
                "Absenteeism Name", 'A', "Comments");

        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();

        Absenteeism absenteeism = new Absenteeism(
                employee,
                reasonsOfAbsenteeism,
                Date.from(atStartOfDayResult.atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(atStartOfDayResult.atZone(ZoneId.systemDefault()).toInstant()));

        when(this.absenteeismRepository.save(any())).thenReturn(absenteeism);
        assertSame(absenteeism, this.absenteeismService.addNewAbsenteeism(new Absenteeism()));
        verify(this.absenteeismRepository).save(any());
    }

    @Test
    void shouldDeleteAbsenteeismById() {
        doNothing().when(this.absenteeismRepository).deleteById(any());
        this.absenteeismService.deleteAbsenteeismById(123L);
        verify(this.absenteeismRepository).deleteById(any());
    }

    @Test
    void shouldNotDeleteAbsenteeismByIdAndThrowsException() {
        doThrow(new EmptyResultDataAccessException(3)).when(this.absenteeismRepository).deleteById(any());
        assertThrows(ResponseStatusException.class, () -> this.absenteeismService.deleteAbsenteeismById(123L));
        verify(this.absenteeismRepository).deleteById(any());
    }

    @Test
    void shouldExportToExcel() throws IOException {
        when(this.absenteeismRepository.findAll()).thenReturn(new ArrayList<>());
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        this.absenteeismService.exportToExcel(mockHttpServletResponse);
        verify(this.absenteeismRepository).findAll();
        assertTrue(mockHttpServletResponse.isCommitted());
        assertNull(mockHttpServletResponse.getRedirectedUrl());
        assertEquals("application/vnd.ms-excel", mockHttpServletResponse.getContentType());
    }

    @Test
    void shouldExportToPdf() throws IOException {
        when(this.absenteeismRepository.findAll()).thenReturn(new ArrayList<>());
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        this.absenteeismService.exportToPDF(mockHttpServletResponse);
        verify(this.absenteeismRepository).findAll();
        assertTrue(mockHttpServletResponse.isCommitted());
        assertNull(mockHttpServletResponse.getRedirectedUrl());
        assertEquals("application/pdf", mockHttpServletResponse.getContentType());
    }
}

