package com.agiklo.oracledatabase.controller;

import com.agiklo.oracledatabase.entity.Supplier;
import com.agiklo.oracledatabase.entity.dto.SupplierDTO;
import com.agiklo.oracledatabase.service.SupplierService;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.agiklo.oracledatabase.controller.ApiMapping.SUPPLIERS_REST_URL;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping(produces="application/json", path = SUPPLIERS_REST_URL)
@AllArgsConstructor
@CrossOrigin(origins="*")
public class SupplierController {
    private final SupplierService supplierService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<SupplierDTO>> getAllSuppliers(){
        return status(HttpStatus.OK).body(supplierService.getAllSuppliers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierDTO> getSupplierById(@PathVariable("id") Long id){
        return status(HttpStatus.OK).body(supplierService.getSupplierById(id));
    }

    @PostMapping(consumes="application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Supplier postNewSuppiler(@RequestBody Supplier supplier){
        return supplierService.addNewSuppiler(supplier);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSupplierById(@PathVariable("id") Long id) throws NotFoundException {
        supplierService.deleteSupplierById(id);
    }

    @GetMapping("/export/excel")
    @ResponseStatus(HttpStatus.CREATED)
    public void exportToExcel(HttpServletResponse response) throws IOException {
        supplierService.exportToExcel(response);
    }

    @GetMapping("/export/pdf")
    @ResponseStatus(HttpStatus.CREATED)
    public void exportToPDF(HttpServletResponse response) throws IOException {
        supplierService.exportToPDF(response);
    }
}
