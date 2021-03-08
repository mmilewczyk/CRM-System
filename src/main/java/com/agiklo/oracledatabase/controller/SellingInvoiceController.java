package com.agiklo.oracledatabase.controller;

import com.agiklo.oracledatabase.entity.SellingInvoice;
import com.agiklo.oracledatabase.entity.dto.SellingInvoiceDTO;
import com.agiklo.oracledatabase.service.SellingInvoiceService;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.agiklo.oracledatabase.controller.ApiMapping.INVOICES_REST_URL;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping(produces="application/json", path = INVOICES_REST_URL)
@AllArgsConstructor
@CrossOrigin(origins="*")
public class SellingInvoiceController {

    private final SellingInvoiceService sellingInvoiceService;

    @GetMapping
    public ResponseEntity<List<SellingInvoiceDTO>> getAllSellingInvoices(){
        return status(HttpStatus.OK).body(sellingInvoiceService.getAllSellingInvoices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SellingInvoiceDTO> getInvoiceById(@PathVariable("id") Long id){
        return status(HttpStatus.OK).body(sellingInvoiceService.getInvoiceById(id));
    }

    @PostMapping(consumes="application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public SellingInvoice postNewInvoice(@RequestBody SellingInvoice sellingInvoice) {
        return sellingInvoiceService.addNewInvoice(sellingInvoice);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInvoiceById(@PathVariable("id") Long id) throws NotFoundException {
        sellingInvoiceService.deleteInvoiceById(id);
    }
}
