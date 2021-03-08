package com.agiklo.oracledatabase.mapper;

import com.agiklo.oracledatabase.entity.Product;
import com.agiklo.oracledatabase.entity.dto.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "productType", source = "productType.fullName")
    ProductDTO mapProductToDto(Product product);
}
