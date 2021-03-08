package com.agiklo.oracledatabase.controller;

import com.agiklo.oracledatabase.entity.Customers;
import com.agiklo.oracledatabase.repository.CustomerRepository;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;


@RunWith(MockitoJUnitRunner.class)
class CustomerControllerTest {

    @Mock
    CustomerRepository customerRepository;

    @InjectMocks
    CustomerController customerController;

    @Before
    private void init(){
        given(customerRepository.findAll()).willReturn(prepareMockData());
    }

//    @Test
//    void getAllCustomers() {
//        //then
//        Assert.assertThat(customerController.getAllCustomers(), Matchers.hasSize(1));
//    }

    private List<Customers> prepareMockData(){
    List<Customers> customersList = new ArrayList<>();
    customersList.add(new Customers("Mateusz", "Milewczyk", "0000", "00000", "Test"));
    return customersList;
    }
}