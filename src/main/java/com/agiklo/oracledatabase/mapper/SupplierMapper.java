package com.agiklo.oracledatabase.mapper;

import com.agiklo.oracledatabase.entity.Supplier;
import com.agiklo.oracledatabase.entity.dto.SupplierDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SupplierMapper {

    @Mapping(target = "typeOfTransport", source = "modeOfTransportCode.fullName")
    SupplierDTO mapSupplierToDTO(Supplier supplier);
}
