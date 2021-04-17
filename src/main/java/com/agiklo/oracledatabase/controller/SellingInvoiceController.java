package com.agiklo.oracledatabase.controller;

import com.agiklo.oracledatabase.entity.SellingInvoice;
import com.agiklo.oracledatabase.entity.dto.SellingInvoiceDTO;
import com.agiklo.oracledatabase.service.SellingInvoiceService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.agiklo.oracledatabase.controller.ApiMapping.INVOICES_REST_URL;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping(produces="application/json", path = INVOICES_REST_URL)
@AllArgsConstructor
@CrossOrigin(origins="*")
public class SellingInvoiceController {

    private final SellingInvoiceService sellingInvoiceService;

    @GetMapping
    @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public ResponseEntity<List<SellingInvoiceDTO>> getAllSellingInvoices(Pageable pageable){
        return status(HttpStatus.OK).body(sellingInvoiceService.getAllSellingInvoices(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public ResponseEntity<SellingInvoiceDTO> getInvoiceById(@PathVariable("id") Long id){
        return status(HttpStatus.OK).body(sellingInvoiceService.getInvoiceById(id));
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SellingInvoiceDTO> editSellingInvoice(@RequestBody SellingInvoice sellingInvoice){
        return status(HttpStatus.OK).body(sellingInvoiceService.editSellingInvoice(sellingInvoice));
    }

    @PostMapping(consumes="application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public SellingInvoice postNewInvoice(@RequestBody SellingInvoice sellingInvoice) {
        return sellingInvoiceService.addNewInvoice(sellingInvoice);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteInvoiceById(@PathVariable("id") Long id){
        sellingInvoiceService.deleteInvoiceById(id);
    }
}
