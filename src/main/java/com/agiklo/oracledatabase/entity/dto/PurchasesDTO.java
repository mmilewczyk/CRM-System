package com.agiklo.oracledatabase.entity.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchasesDTO {

    private Long id;
    private Long customerId;
    private String customerFirstName;
    private String customerLastName;
    private String purchaseDate;
    private String invoiceId;
    private String invoiceDate;
    private String invoiceCustomerId;
    private String invoiceCustomerFirstName;
    private String invoiceCustomerLastName;
    private String invoiceNetWorth;
    private String invoiceGrossValue;
    private String invoiceCurrency;
}