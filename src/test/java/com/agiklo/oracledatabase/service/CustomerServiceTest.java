package com.agiklo.oracledatabase.service;

import com.agiklo.oracledatabase.entity.Customers;
import com.agiklo.oracledatabase.entity.dto.CustomerDTO;
import com.agiklo.oracledatabase.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("dev")
class CustomerServiceTest {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    @Transactional
    void shouldGetCustomerById() {
        Customers customers = prepareCustomerToTest();
        customerRepository.save(customers);
        //when
        CustomerDTO customer = customerService.getCustomerById(customers.getId());
        //then
        assertThat(customer).isNotNull();
        assertThat(customer.getFirstname()).isEqualTo("Vidkun");
        assertThat(customer.getCity()).isEqualTo("Oslo");
    }

    Customers prepareCustomerToTest(){
        Customers newCustomer = new Customers();
        newCustomer.setFirstname("Vidkun");
        newCustomer.setLastname("Rikardson");
        newCustomer.setPesel("97542899482");
        newCustomer.setCity("Oslo");
        newCustomer.setZipCode("300-20");
        return newCustomer;
    }
}
