package com.agiklo.oracledatabase.controller;

import com.agiklo.oracledatabase.entity.dto.EmployeeDTO;
import com.agiklo.oracledatabase.service.EmployeeService;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.io.IOException;
import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;

import static com.agiklo.oracledatabase.controller.ApiMapping.EMPLOYEES_REST_URL;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping(produces="application/json", path = EMPLOYEES_REST_URL)
@AllArgsConstructor
@CrossOrigin(origins="*")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees(){
        return status(HttpStatus.OK).body(employeeService.getAllEmployees());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable("id") Long id){
        return status(HttpStatus.OK).body(employeeService.getEmployeeById(id));
    }

    @GetMapping("/")
    public ResponseEntity<Set<EmployeeDTO>> findEmployeesByFirstname(@RequestParam("firstname") String firstName) {
        return status(HttpStatus.OK).body(employeeService.findEmployeesByFirstname(firstName));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployeeById(@PathVariable("id") Long id) throws NotFoundException {
        employeeService.deleteEmployeeById(id);
    }

    @GetMapping("/export/excel")
    @ResponseStatus(HttpStatus.CREATED)
    public void exportToExcel(HttpServletResponse response) throws IOException {
        employeeService.exportToExcel(response);
    }

    @GetMapping("/export/pdf")
    @ResponseStatus(HttpStatus.CREATED)
    public void exportToPDF(HttpServletResponse response) throws IOException {
        employeeService.exportToPDF(response);
    }
}
