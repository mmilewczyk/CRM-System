package com.agiklo.oracledatabase.mapper;

import com.agiklo.oracledatabase.entity.Customers;
import com.agiklo.oracledatabase.entity.dto.CustomerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "pesel", ignore = true)
    CustomerDTO mapCustomersToDto(Customers customers);

    @Mapping(target = "Customers.pesel")
    Customers mapCustomerDTOtoCustomers(CustomerDTO customerDTO);
}
