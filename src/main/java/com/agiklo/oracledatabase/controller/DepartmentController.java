package com.agiklo.oracledatabase.controller;

import com.agiklo.oracledatabase.entity.Departments;
import com.agiklo.oracledatabase.entity.dto.DepartmentDTO;
import com.agiklo.oracledatabase.service.DepartmentService;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.agiklo.oracledatabase.controller.ApiMapping.DEPARTMENTS_REST_URL;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping(produces="application/json", path = DEPARTMENTS_REST_URL)
@AllArgsConstructor
@CrossOrigin(origins="*")
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments(){
        return status(HttpStatus.OK).body(departmentService.getAllDepartments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDTO> getDepartmentById(@PathVariable("id") Long id){
        return status(HttpStatus.OK).body(departmentService.getDepartmentById(id));
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
