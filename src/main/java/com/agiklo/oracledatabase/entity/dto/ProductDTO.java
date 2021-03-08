package com.agiklo.oracledatabase.entity.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    private Long id;
    private String name;
    private String productType;
    private Double sellingPrice;
    private Double purchasePrice;
    private Double taxRate;
}