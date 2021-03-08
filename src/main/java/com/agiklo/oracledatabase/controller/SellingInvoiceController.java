package com.agiklo.oracledatabase.controller;

import com.agiklo.oracledatabase.entity.SellingInvoice;
import com.agiklo.oracledatabase.service.SellingInvoiceService;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.agiklo.oracledatabase.controller.ApiMapping.INVOICES_REST_URL;

@RestController
@RequestMapping(produces="application/json", path = INVOICES_REST_URL)
@AllArgsConstructor
@CrossOrigin(origins="*")
public class SellingInvoiceController {

    private final SellingInvoiceService sellingInvoiceService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<SellingInvoice> getAllSellingInvoices(){
        return sellingInvoiceService.getAllSellingInvoices();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SellingInvoice> getInvoiceById(@PathVariable("id") Long id){
        Optional<SellingInvoice> optionalSellingInvoice = sellingInvoiceService.getInvoiceById(id);
        return optionalSellingInvoice
                .map(invoice -> new ResponseEntity<>(invoice, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
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
