package com.agiklo.oracledatabase.entity.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierDTO {

    private Long supplierId;
    private String supplierName;
    private String typeOfTransport;
    private String activityStatus;

}
