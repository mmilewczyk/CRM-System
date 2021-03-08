package com.agiklo.oracledatabase.controller;

import com.agiklo.oracledatabase.entity.Employee;
import com.agiklo.oracledatabase.service.EmployeeService;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import java.io.IOException;
import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;

import static com.agiklo.oracledatabase.controller.ApiMapping.EMPLOYEES_REST_URL;

@RestController
@RequestMapping(produces="application/json", path = EMPLOYEES_REST_URL)
@AllArgsConstructor
@CrossOrigin(origins="*")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Employee> getAllEmployees(){
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") Long id){
        Optional<Employee> optionalEmployees = employeeService.getEmployeeById(id);
        return optionalEmployees
                .map(employee -> new ResponseEntity<>(employee, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/")
    public Set<Employee> findEmployeesByFirstname(@RequestParam("firstname") String firstName) throws NotFoundException {
        return employeeService.findEmployeesByFirstname(firstName);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployeeById(@PathVariable("id") Long id) throws NotFoundException {
        employeeService.deleteEmployeeById(id);
    }

    @GetMapping("/export/pdf")
    @ResponseStatus(HttpStatus.CREATED)
    public void exportToPDF(HttpServletResponse response) throws IOException {
        employeeService.exportToPDF(response);
    }
}
