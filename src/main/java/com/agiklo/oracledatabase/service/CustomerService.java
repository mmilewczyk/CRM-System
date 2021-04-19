package com.agiklo.oracledatabase.service;

import com.agiklo.oracledatabase.entity.Customers;
import com.agiklo.oracledatabase.entity.dto.CustomerDTO;
import com.agiklo.oracledatabase.exports.pdf.ExportCustomersToPDF;
import com.agiklo.oracledatabase.exports.excel.ExportCustomersToXLSX;
import com.agiklo.oracledatabase.mapper.CustomerMapper;
import com.agiklo.oracledatabase.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Mateusz Milewczyk (agiklo)
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class CustomerService implements CurrentTimeInterface{

    /**
     * were injected by the constructor using the lombok @AllArgsContrustor annotation
     */
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    /**
     * The method is to retrieve all customers from the database and display them.
     *
     * After downloading all the data about the customer,
     * the data is mapped to dto which will display only those needed
     * @return list of all customers with specification of data in CustomerDTO
     */
    @Transactional(readOnly = true)
    public List<CustomerDTO> getAllCustomers(Pageable pageable){
        return customerRepository.findAll(pageable)
                .stream()
                .map(customerMapper::mapCustomersToDto)
                .collect(Collectors.toList());
    }

    /**
     * The method is to download a specific customer from the database and display it.
     * After downloading all the data about the customer,
     * the data is mapped to dto which will display only those needed
     *
     * @param id id of the customer to be searched for
     * @throws ResponseStatusException if the id of the customer you are looking for does not exist throws 404 status
     * @return detailed data about a specific customer
     */
    @Transactional(readOnly = true)
    public CustomerDTO getCustomerById(Long id) {
        Customers customer = customerRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Customer cannot be found, the specified id does not exist"));
        return customerMapper.mapCustomersToDto(customer);

    }

    /**
     * The method is to retrieve customers whose have the firstname specified by the user.
     * After downloading all the data about the customer,
     * the data is mapped to dto which will display only those needed
     * @param firstName firstname of the customer
     * @return details of specific customers
     */
    @Transactional(readOnly = true)
    public List<CustomerDTO> findCustomersByFirstname(String firstName, Pageable pageable){
        return customerRepository.findCustomersByFirstnameContainingIgnoreCase(firstName, pageable)
                .stream()
                .map(customerMapper::mapCustomersToDto)
                .collect(Collectors.toList());
    }

    /**
     * The task of the method is to add a customer to the database.
     * @param customer requestbody of the customer to be saved
     * @return saving the customer to the database
     */
    public Customers addNewCustomer(CustomerDTO customer) {
        return customerRepository.save(customerMapper.mapCustomerDTOtoCustomers(customer));
    }

    /**
     * Method deletes the selected customer by id
     * @param id id of the customer to be deleted
     * @throws ResponseStatusException if id of the customer is incorrect throws 404 status with message
     */
    public void deleteCustomerById(Long id) {
        try{
            customerRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The specified id does not exist");
        }
    }

    /**
     * The purpose of the method is to set the details of the
     * excel file that will be exported for download and then download it.
     * @param response response to determine the details of the file
     * @throws IOException if incorrect data is sent to the file
     */
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=customers_" + getCurrentDateTime() + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<Customers> customersList = customerRepository.findAll();

        ExportCustomersToXLSX exporter = new ExportCustomersToXLSX(customersList);
        exporter.export(response);
    }

    /**
     * The purpose of the method is to set the details of the
     * pdf file that will be exported for download and then download it.
     * @param response response to determine the details of the file
     * @throws IOException if incorrect data is sent to the file
     */
    public void exportToPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=customers_" + getCurrentDateTime() + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<Customers> customersList = customerRepository.findAll();

        ExportCustomersToPDF exporter = new ExportCustomersToPDF(customersList);
        exporter.export(response);
    }

    @Transactional
    public CustomerDTO editCustomer(Customers customers) {
        Customers editedCustomer = customerRepository.findById(customers.getId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer does not exist"));
        editedCustomer.setFirstname(customers.getFirstname());
        editedCustomer.setLastname(customers.getLastname());
        editedCustomer.setCity(customers.getCity());
        editedCustomer.setZipCode(customers.getZipCode());
        editedCustomer.setPesel(customers.getPesel());
        return customerMapper.mapCustomersToDto(editedCustomer);
    }
}
