package com.agiklo.oracledatabase.controller;

import com.agiklo.oracledatabase.entity.Absenteeism;
import com.agiklo.oracledatabase.entity.dto.AbsenteeismDTO;
import com.agiklo.oracledatabase.service.AbsenteeismService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.agiklo.oracledatabase.controller.ApiMapping.ABSENTEEISMS_REST_URL;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping(produces="application/json", path = ABSENTEEISMS_REST_URL)
@AllArgsConstructor
@CrossOrigin(origins="*")
public class AbsenteeismController {
    private final AbsenteeismService absenteeismService;

    @GetMapping
    @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public ResponseEntity<List<AbsenteeismDTO>> getAllAbsenteeisms(Pageable pageable){
        return status(HttpStatus.OK).body(absenteeismService.getAllAbsenteeisms(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public ResponseEntity<AbsenteeismDTO> getAbsenteeismById(@PathVariable("id") Long id){
        return status(HttpStatus.OK).body(absenteeismService.getAbsenteeismById(id));
    }

    @PostMapping(consumes="application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public Absenteeism postNewAbsenteeism(@RequestBody Absenteeism absenteeism) {
        return absenteeismService.addNewAbsenteeism(absenteeism);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteAbsenteeismById(@PathVariable("id") Long id) {
        absenteeismService.deleteAbsenteeismById(id);
    }

    @GetMapping("/export/excel")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        absenteeismService.exportToExcel(response);
    }

    @GetMapping("/export/pdf")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public void exportToPDF(HttpServletResponse response) throws IOException {
        absenteeismService.exportToPDF(response);
    }
}
