package com.agiklo.oracledatabase.entity.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SellingInvoiceDTO {

    private Long id;
    private String invoiceDate;
    private Long customerId;
    private String customerFirstName;
    private String customerLastName;
    private Double netWorth;
    private Double grossValue;
    private Double taxRate;
    private String currency;

}
