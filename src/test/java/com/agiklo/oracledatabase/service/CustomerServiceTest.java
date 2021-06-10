package com.agiklo.oracledatabase.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.agiklo.oracledatabase.entity.Customers;
import com.agiklo.oracledatabase.entity.dto.CustomerDTO;
import com.agiklo.oracledatabase.mapper.CustomerMapper;
import com.agiklo.oracledatabase.repository.CustomerRepository;

import java.io.IOException;
import java.util.ArrayList;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

@ContextConfiguration(classes = {CustomerService.class})
@ExtendWith(SpringExtension.class)
@Execution(ExecutionMode.CONCURRENT)
class CustomerServiceTest {

    @MockBean
    private CustomerMapper customerMapper;

    @MockBean
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerService customerService;

    @Test
    void shouldDoesNotFindCustomerByFirstname() {
        //when
        when(this.customerRepository.findCustomersByFirstnameContainingIgnoreCase(anyString(), any()))
                .thenReturn(new ArrayList<>());
        //then
        assertTrue(this.customerService.findCustomersByFirstname("Mateusz", null).isEmpty());
        verify(this.customerRepository).findCustomersByFirstnameContainingIgnoreCase(anyString(), any());
    }

    @Test
    void shouldFindCustomersByFirstname() {
        // given
        Customers customers = new Customers("Mateusz", "Milewczyk", "Pesel", "21-654", "Warsaw");

        ArrayList<Customers> customersList = new ArrayList<>();
        customersList.add(customers);

        // when
        when(this.customerRepository.findCustomersByFirstnameContainingIgnoreCase(anyString(), any()))
                .thenReturn(customersList);
        when(this.customerMapper.mapCustomersToDto(any()))
                .thenReturn(new CustomerDTO("Mateusz", "Milewczyk", "21-654", "Warsaw"));

        //then
        assertEquals(1, this.customerService.findCustomersByFirstname("Mateusz", null).size());
        verify(this.customerMapper).mapCustomersToDto(any());
        verify(this.customerRepository).findCustomersByFirstnameContainingIgnoreCase(anyString(), any());
    }

    @Test
    void shouldAddNewCustomer() {
        // given
        Customers customer = new Customers("Mateusz", "Milewczyk", "Pesel", "21-654", "Warsaw");
        Customers customer1 = new Customers("Mateusz", "Milewczyk", "Pesel", "21-654", "Warsaw");

        // when
        when(this.customerRepository.save(any())).thenReturn(customer);
        when(this.customerMapper.mapCustomerDTOtoCustomers(any())).thenReturn(customer1);

        // then
        assertEquals(customer, this.customerService.addNewCustomer(
                new CustomerDTO("Mateusz", "Milewczyk", "21-654", "Warsaw")));
        verify(this.customerMapper).mapCustomerDTOtoCustomers(any());
        verify(this.customerRepository).save(any());
    }

    @Test
    void shouldDeleteCustomerById() {
        // when
        doNothing().when(this.customerRepository).deleteById(any());
        this.customerService.deleteCustomerById(123L);

        // then
        verify(this.customerRepository).deleteById(any());
    }

    @Test
    void shouldDoesNotDeleteCustomerAndThrowsExceptions() {
        // when
        // then
        doThrow(new EmptyResultDataAccessException(3)).when(this.customerRepository).deleteById(any());
        assertThrows(ResponseStatusException.class, () -> this.customerService.deleteCustomerById(123L));
        verify(this.customerRepository).deleteById(any());
    }

    @Test
    void shouldExportCustomersToExcel() throws IOException {
        // given
        // when
        when(this.customerRepository.findAll()).thenReturn(new ArrayList<>());
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        this.customerService.exportToExcel(mockHttpServletResponse);

        // then
        verify(this.customerRepository).findAll();
        assertTrue(mockHttpServletResponse.isCommitted());
        assertNull(mockHttpServletResponse.getRedirectedUrl());
        assertEquals("application/vnd.ms-excel", mockHttpServletResponse.getContentType());
    }

    @Test
    void shouldExportCustomersToPdf() throws IOException {
        // given
        // when
        when(this.customerRepository.findAll()).thenReturn(new ArrayList<>());
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        this.customerService.exportToPDF(mockHttpServletResponse);

        // then
        verify(this.customerRepository).findAll();
        assertTrue(mockHttpServletResponse.isCommitted());
        assertNull(mockHttpServletResponse.getRedirectedUrl());
        assertEquals("application/pdf", mockHttpServletResponse.getContentType());
    }

    @Test
    void shouldEditCustomer() {
        // given
        Customers customer = new Customers("Mateusz", "Milewczyk", "Pesel", "21-654", "Warsaw");
        CustomerDTO customerDTO = new CustomerDTO("Mateusz", "Milewczyk", "21-654", "Warsaw");
        Optional<Customers> ofResult = Optional.of(customer);

        // when
        when(this.customerRepository.findById(any())).thenReturn(ofResult);
        when(this.customerMapper.mapCustomersToDto(any())).thenReturn(customerDTO);

        // then
        assertEquals(customerDTO, this.customerService.editCustomer(
                new Customers("Mateusz", "Milewczyk", "Pesel", "21-654", "Warsaw")));
        verify(this.customerMapper).mapCustomersToDto(any());
        verify(this.customerRepository).findById(any());
    }

    @Test
    void shouldDoesNotEditCustomerAndThrowException() {
        // given
        // when
        when(this.customerRepository.findById(any())).thenReturn(Optional.empty());
        when(this.customerMapper.mapCustomersToDto(any()))
                .thenReturn(new CustomerDTO("Jan", "Kowalski", "21-654", "Cracow"));

        // then
        assertThrows(ResponseStatusException.class, () -> this.customerService.editCustomer(
                new Customers("Jan", "Kowalski", "Pesel", "21-654", "Cracow")));
        verify(this.customerRepository).findById(any());
    }
}

