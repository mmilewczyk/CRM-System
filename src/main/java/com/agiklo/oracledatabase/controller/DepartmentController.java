package com.agiklo.oracledatabase.controller;

import com.agiklo.oracledatabase.entity.Departments;
import com.agiklo.oracledatabase.entity.dto.DepartmentDTO;
import com.agiklo.oracledatabase.service.DepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments(Pageable pageable){
        return status(HttpStatus.OK).body(departmentService.getAllDepartments(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public ResponseEntity<DepartmentDTO> getDepartmentById(@PathVariable("id") Long id){
        return status(HttpStatus.OK).body(departmentService.getDepartmentById(id));
    }

    @GetMapping("/")
    @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public ResponseEntity<List<DepartmentDTO>> getAllDepartmentsByName(@RequestParam("name") String name, Pageable pageable){
        return status(HttpStatus.OK).body(departmentService.getAllDepartmentsByName(name, pageable));
    }

    @PostMapping(consumes="application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public Departments postNewDepartment(@RequestBody Departments department) {
        return departmentService.addNewDepartment(department);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteDepartmentById(@PathVariable("id") Long id) {
        departmentService.deleteDepartmentById(id);
    }

    @GetMapping("/export/excel")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        departmentService.exportToExcel(response);
    }

    @GetMapping("/export/pdf")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public void exportToPDF(HttpServletResponse response) throws IOException {
        departmentService.exportToPDF(response);
    }
}
