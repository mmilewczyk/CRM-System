package com.agiklo.oracledatabase.controller;

import com.agiklo.oracledatabase.entity.Customers;
import com.agiklo.oracledatabase.entity.dto.CustomerDTO;
import com.agiklo.oracledatabase.service.CustomerService;
import lombok.AllArgsConstructor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;


import static com.agiklo.oracledatabase.controller.ApiMapping.CUSTOMERS_REST_URL;
import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping(produces="application/json", path = CUSTOMERS_REST_URL)
@AllArgsConstructor
@CrossOrigin(origins="*")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers(){
        return status(HttpStatus.OK).body(customerService.getAllCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable("id") Long id) {
        return status(HttpStatus.OK).body(customerService.getCustomerById(id));
    }

    @GetMapping(path = "/")
    public ResponseEntity<Set<CustomerDTO>> findCustomersByFirstname(
            @RequestParam("firstname") String firstName){
        return status(HttpStatus.OK).body(customerService.findCustomersByFirstname(firstName));
    }

    @PostMapping(consumes="application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Customers postNewCustomer(@RequestBody CustomerDTO customer) {
        return customerService.addNewCustomer(customer);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomerById(@PathVariable("id") Long id) {
        customerService.deleteCustomerById(id);
    }

    @GetMapping("/export/excel")
    @ResponseStatus(HttpStatus.CREATED)
    public void exportToExcel(HttpServletResponse response) throws IOException {
        customerService.exportToExcel(response);
    }

    @GetMapping("/export/pdf")
    @ResponseStatus(HttpStatus.CREATED)
    public void exportToPDF(HttpServletResponse response) throws IOException {
        customerService.exportToPDF(response);
    }
}
