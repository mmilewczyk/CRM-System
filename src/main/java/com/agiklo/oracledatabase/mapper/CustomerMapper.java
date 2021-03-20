package com.agiklo.oracledatabase.mapper;

import com.agiklo.oracledatabase.entity.Customers;
import com.agiklo.oracledatabase.entity.dto.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDTO mapCustomersToDto(Customers customers);

    Customers mapCustomerDTOtoCustomers(CustomerDTO customerDTO);
}
