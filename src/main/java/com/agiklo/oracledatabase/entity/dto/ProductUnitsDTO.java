package com.agiklo.oracledatabase.entity.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductUnitsDTO {

    private Long id;
    private Long productId;
    private String productName;
    private String unitOfMeasure;
    private String alternativeUnitOfMeasure;
    private Double conversionFactor;
}
