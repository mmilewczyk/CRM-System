package com.agiklo.oracledatabase.service;

import com.agiklo.oracledatabase.entity.dto.CustomerDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("dev")
class CustomerServiceTest {

    @Autowired
    private CustomerService customerService;

    @Test
    void shouldGetCustomerById() {
        //when
        CustomerDTO customer = customerService.getCustomerById(3L);
        //then
        assertThat(customer).isNotNull();
        assertThat(customer.getFirstname()).isEqualTo("Kazimiera");
        assertThat(customer.getCity()).isEqualTo("Gdańsk");
    }
}
