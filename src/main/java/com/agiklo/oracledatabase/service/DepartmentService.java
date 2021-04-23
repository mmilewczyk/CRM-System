package com.agiklo.oracledatabase.service;

import com.agiklo.oracledatabase.entity.Departments;
import com.agiklo.oracledatabase.entity.dto.DepartmentDTO;
import com.agiklo.oracledatabase.exports.pdf.ExportDepartmentsToPDF;
import com.agiklo.oracledatabase.exports.excel.ExportDepartmentsToXLSX;
import com.agiklo.oracledatabase.mapper.DepartmentMapper;
import com.agiklo.oracledatabase.repository.DepartmentsRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Mateusz Milewczyk (agiklo)
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class DepartmentService implements CurrentTimeInterface{

    /**
     * were injected by the constructor using the lombok @AllArgsContrustor annotation
     */
    private final DepartmentsRepository departmentsRepository;
    private final DepartmentMapper departmentMapper;

    /**
     * The method is to retrieve all departments from the database and display them.
     *
     * After downloading all the data about the department,
     * the data is mapped to dto which will display only those needed
     * @return list of all departments with specification of data in DepartmentDTO
     */
    @Transactional(readOnly = true)
    public List<DepartmentDTO> getAllDepartments(Pageable pageable){
        return departmentsRepository.findAllBy(pageable)
                .stream()
                .map(departmentMapper::mapDepartmentToDto)
                .collect(Collectors.toList());
    }

    /**
     * The method is to download a specific department from the database and display it.
     * After downloading all the data about the department,
     * the data is mapped to dto which will display only those needed
     *
     * @param id id of the department to be searched for
     * @throws ResponseStatusException if the id of the department you are looking for does not exist throws 404 status
     * @return detailed data about a specific department
     */
    @Transactional(readOnly = true)
    public DepartmentDTO getDepartmentById(Long id) {
        Departments departments = departmentsRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department cannot be found, the specified id does not exist"));
        return departmentMapper.mapDepartmentToDto(departments);
    }

    /**
     * The task of the method is to add a department to the database.
     * @param department requestbody of the department to be saved
     * @return saving the department to the database
     */
    public Departments addNewDepartment(Departments department) {
        return departmentsRepository.save(department);
    }

    /**
     * Method deletes the selected department by id
     * @param id id of the department to be deleted
     * @throws ResponseStatusException if id of the department is incorrect throws 404 status with message
     */
    public void deleteDepartmentById(Long id) {
        try{
            departmentsRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The specified id does not exist");
        }
    }

    /**
     * The purpose of the method is to set the details of the
     * excel file that will be exported for download and then download it.
     * @param response response to determine the details of the file
     * @throws IOException if incorrect data is sent to the file
     */
    public void exportToExcel(HttpServletResponse response) throws IOException{
        response.setContentType("application/vnd.ms-excel");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=departments_" + getCurrentDateTime() + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<Departments> departmentsList = departmentsRepository.findAll();

        ExportDepartmentsToXLSX exporter = new ExportDepartmentsToXLSX(departmentsList);
        exporter.export(response);
    }

    /**
     * The purpose of the method is to set the details of the
     * pdf file that will be exported for download and then download it.
     * @param response response to determine the details of the file
     * @throws IOException if incorrect data is sent to the file
     */
    public void exportToPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=departments_" + getCurrentDateTime() + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<Departments> departmentsList = departmentsRepository.findAll();

        ExportDepartmentsToPDF exporter = new ExportDepartmentsToPDF(departmentsList);
        exporter.export(response);
    }

    /**
     * The method is to retrieve departments whose have the name specified by the user.
     * After downloading all the data about the department,
     * the data is mapped to dto which will display only those needed
     * @param name name of the department
     * @return details of specific departments
     */
    public List<DepartmentDTO> getAllDepartmentsByName(String name, Pageable pageable) {
        return departmentsRepository.getDepartmentsByDepartmentNameContainingIgnoreCase(name, pageable)
                .stream()
                .map(departmentMapper::mapDepartmentToDto)
                .collect(Collectors.toList());
    }
}
