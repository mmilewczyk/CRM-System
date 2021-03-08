package com.agiklo.oracledatabase.service;

import com.agiklo.oracledatabase.entity.Customers;
import com.agiklo.oracledatabase.entity.dto.CustomerDTO;
import com.agiklo.oracledatabase.exports.ExportCustomersToPDF;
import com.agiklo.oracledatabase.mapper.CustomerMapper;
import com.agiklo.oracledatabase.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Transactional(readOnly = true)
    public List<CustomerDTO> getAllCustomers(){
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::mapCustomersToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CustomerDTO getCustomerById(Long id) {
        Customers customer = customerRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer Not Found"));
        return customerMapper.mapCustomersToDto(customer);

    }

    @Transactional(readOnly = true)
    public Set<CustomerDTO> findCustomersByFirstname(String firstName){
        return customerRepository.findCustomersByFirstnameLike(firstName)
                .stream()
                .map(customerMapper::mapCustomersToDto)
                .collect(Collectors.toSet());
    }

    public Customers addNewCustomer(CustomerDTO customer) {
        return customerRepository.save(customerMapper.mapCustomerDTOtoCustomers(customer));
    }

    public void deleteCustomerById(Long id) {
        try{
            customerRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The specified id does not exist");
        }
    }

    public void exportToPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=customers_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<Customers> customersList = customerRepository.findAll();

        ExportCustomersToPDF exporter = new ExportCustomersToPDF(customersList);
        exporter.export(response);
    }
}
