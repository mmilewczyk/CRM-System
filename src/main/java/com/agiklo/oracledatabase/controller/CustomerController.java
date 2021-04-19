package com.agiklo.oracledatabase.controller;

import com.agiklo.oracledatabase.entity.Customers;
import com.agiklo.oracledatabase.entity.dto.CustomerDTO;
import com.agiklo.oracledatabase.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.agiklo.oracledatabase.controller.ApiMapping.CUSTOMERS_REST_URL;
import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping(produces="application/json", path = CUSTOMERS_REST_URL)
@AllArgsConstructor
@CrossOrigin(origins="*")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public ResponseEntity<List<CustomerDTO>> getAllCustomers(Pageable pageable){
        return status(HttpStatus.OK).body(customerService.getAllCustomers(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable("id") Long id) {
        return status(HttpStatus.OK).body(customerService.getCustomerById(id));
    }

    @GetMapping(path = "/")
    @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public ResponseEntity<List<CustomerDTO>> findCustomersByFirstname(
            @RequestParam("firstname") String firstName, Pageable pageable){
        return status(HttpStatus.OK).body(customerService.findCustomersByFirstname(firstName, pageable));
    }

    @PutMapping
    public ResponseEntity<CustomerDTO> editCustomer(@RequestBody Customers customers){
        return status(HttpStatus.OK).body(customerService.editCustomer(customers));
    }

    @PostMapping(consumes="application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public Customers postNewCustomer(@RequestBody CustomerDTO customer) {
        return customerService.addNewCustomer(customer);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteCustomerById(@PathVariable("id") Long id) {
        customerService.deleteCustomerById(id);
    }

    @GetMapping("/export/excel")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        customerService.exportToExcel(response);
    }

    @GetMapping("/export/pdf")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public void exportToPDF(HttpServletResponse response) throws IOException {
        customerService.exportToPDF(response);
    }
}
