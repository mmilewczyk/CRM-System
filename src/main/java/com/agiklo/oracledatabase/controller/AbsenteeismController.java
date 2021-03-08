package com.agiklo.oracledatabase.controller;

import com.agiklo.oracledatabase.entity.Absenteeism;
import com.agiklo.oracledatabase.entity.dto.AbsenteeismDTO;
import com.agiklo.oracledatabase.service.AbsenteeismService;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<AbsenteeismDTO>> getAllAbsenteeisms(){
        return status(HttpStatus.OK).body(absenteeismService.getAllAbsenteeisms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AbsenteeismDTO> getAbsenteeismById(@PathVariable("id") Long id){
        return status(HttpStatus.OK).body(absenteeismService.getAbsenteeismById(id));
    }

    @PostMapping(consumes="application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Absenteeism postNewAbsenteeism(@RequestBody Absenteeism absenteeism) {
        return absenteeismService.addNewAbsenteeism(absenteeism);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAbsenteeismById(@PathVariable("id") Long id) throws NotFoundException {
        absenteeismService.deleteAbsenteeismById(id);
    }

    @GetMapping("/export/pdf")
    @ResponseStatus(HttpStatus.CREATED)
    public void exportToPDF(HttpServletResponse response) throws IOException {
        absenteeismService.exportToPDF(response);
    }
}
