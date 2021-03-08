package com.agiklo.oracledatabase.entity.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchasesPositionsDTO {

    private Long id;
    private Double amount;
    private Long productId;
    private String productName;
    private Long purchaseId;
    private Long customerId;
    private String sellingPrice;
    private String purchaseDate;
    private Character reclamationExist;

}