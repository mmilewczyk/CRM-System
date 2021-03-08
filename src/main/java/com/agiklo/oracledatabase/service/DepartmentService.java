package com.agiklo.oracledatabase.service;

import com.agiklo.oracledatabase.entity.Departments;
import com.agiklo.oracledatabase.exports.ExportDepartmentsToPDF;
import com.agiklo.oracledatabase.repository.DepartmentsRepository;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DepartmentService {

    private final DepartmentsRepository departmentsRepository;

    public List<Departments> getAllDepartments(){
        return departmentsRepository.findAll();
    }

    public Optional<Departments> getDepartmentById(Long id) {
        return departmentsRepository.findById(id);
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

    public void exportToPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=departments_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<Departments> departmentsList = departmentsRepository.findAll();

        ExportDepartmentsToPDF exporter = new ExportDepartmentsToPDF(departmentsList);
        exporter.export(response);
    }
}
