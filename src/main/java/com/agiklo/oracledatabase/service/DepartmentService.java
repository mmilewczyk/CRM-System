package com.agiklo.oracledatabase.service;

import com.agiklo.oracledatabase.entity.Departments;
import com.agiklo.oracledatabase.entity.dto.DepartmentDTO;
import com.agiklo.oracledatabase.exports.pdf.ExportDepartmentsToPDF;
import com.agiklo.oracledatabase.exports.excel.ExportDepartmentsToXLSX;
import com.agiklo.oracledatabase.mapper.DepartmentMapper;
import com.agiklo.oracledatabase.repository.DepartmentsRepository;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DepartmentService implements CurrentTimeInterface{

    private final DepartmentsRepository departmentsRepository;
    private final DepartmentMapper departmentMapper;

    @Transactional(readOnly = true)
    public List<DepartmentDTO> getAllDepartments(){
        return departmentsRepository.findAll()
                .stream()
                .map(departmentMapper::mapDepartmentToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DepartmentDTO getDepartmentById(Long id) {
        Departments departments = departmentsRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department cannot be found, the specified id does not exist"));
        return departmentMapper.mapDepartmentToDto(departments);
    }

    public Departments addNewDepartment(Departments department) {
        return departmentsRepository.save(department);
    }

    public void deleteDepartmentById(Long id) throws NotFoundException {
        try{
            departmentsRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("The specified id does not exist");
        }
    }

    public void exportToExcel(HttpServletResponse response) throws IOException{
        response.setContentType("application/vnd.ms-excel");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=departments_" + getCurrentDateTime() + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<Departments> departmentsList = departmentsRepository.findAll();

        ExportDepartmentsToXLSX exporter = new ExportDepartmentsToXLSX(departmentsList);
        exporter.export(response);
    }

    public void exportToPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=departments_" + getCurrentDateTime() + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<Departments> departmentsList = departmentsRepository.findAll();

        ExportDepartmentsToPDF exporter = new ExportDepartmentsToPDF(departmentsList);
        exporter.export(response);
    }
}
