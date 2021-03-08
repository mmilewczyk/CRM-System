package com.agiklo.oracledatabase.mapper;

import com.agiklo.oracledatabase.entity.PurchasesPositions;
import com.agiklo.oracledatabase.entity.dto.PurchasesPositionsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PurchasesPositionsMapper {

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "purchaseId", source = "purchases.id")
    @Mapping(target = "customerId", source = "purchases.customer.id")
    @Mapping(target = "purchaseDate", source = "purchases.purchaseDate")
    @Mapping(target = "sellingPrice", source = "product.sellingPrice")
    PurchasesPositionsDTO mapPurchasesPositionsToDTO(PurchasesPositions purchasesPositions);
}