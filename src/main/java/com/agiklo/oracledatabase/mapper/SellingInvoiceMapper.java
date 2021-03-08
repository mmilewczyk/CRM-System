package com.agiklo.oracledatabase.mapper;

import com.agiklo.oracledatabase.entity.SellingInvoice;
import com.agiklo.oracledatabase.entity.dto.SellingInvoiceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SellingInvoiceMapper {

    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "customerFirstName", source = "customer.firstname")
    @Mapping(target = "customerLastName", source = "customer.lastname")
    SellingInvoiceDTO mapSellingInvoiceToDTO(SellingInvoice sellingInvoice);
}
