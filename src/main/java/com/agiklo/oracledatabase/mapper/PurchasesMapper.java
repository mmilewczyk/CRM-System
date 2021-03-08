package com.agiklo.oracledatabase.mapper;

import com.agiklo.oracledatabase.entity.Purchases;
import com.agiklo.oracledatabase.entity.dto.PurchasesDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PurchasesMapper {

    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "customerFirstName", source = "customer.firstname")
    @Mapping(target = "customerLastName", source = "customer.lastname")
    @Mapping(target = "invoiceId", source = "invoice.id")
    @Mapping(target = "invoiceDate", source = "invoice.invoiceDate")
    @Mapping(target = "invoiceCustomerId", source = "invoice.customer.id")
    @Mapping(target = "invoiceCustomerFirstName", source = "invoice.customer.firstname")
    @Mapping(target = "invoiceCustomerLastName", source = "invoice.customer.lastname")
    @Mapping(target = "invoiceNetWorth", source = "invoice.netWorth")
    @Mapping(target = "invoiceGrossValue", source = "invoice.grossValue")
    @Mapping(target = "invoiceCurrency", source = "invoice.currency")
    PurchasesDTO mapPurchasesToDTO(Purchases purchases);
}
