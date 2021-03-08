package com.agiklo.oracledatabase.controller;

import com.agiklo.oracledatabase.entity.Departments;
import com.agiklo.oracledatabase.service.DepartmentService;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.agiklo.oracledatabase.controller.ApiMapping.DEPARTMENTS_REST_URL;

@RestController
@RequestMapping(produces="application/json", path = DEPARTMENTS_REST_URL)
@AllArgsConstructor
@CrossOrigin(origins="*")
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Departments> getAllDepartments(){
        return departmentService.getAllDepartments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Departments> getDepartmentById(@PathVariable("id") Long id){
        Optional<Departments> optionalDepartment = departmentService.getDepartmentById(id);
        return optionalDepartment
                .map(departments -> new ResponseEntity<>(departments, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(consumes="application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Departments postNewDepartment(@RequestBody Departments department) {
        return departmentService.addNewDepartment(department);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDepartmentById(@PathVariable("id") Long id) throws NotFoundException {
        departmentService.deleteDepartmentById(id);
    }

    @GetMapping("/export/pdf")
    @ResponseStatus(HttpStatus.CREATED)
    public void exportToPDF(HttpServletResponse response) throws IOException {
        departmentService.exportToPDF(response);
    }
}
