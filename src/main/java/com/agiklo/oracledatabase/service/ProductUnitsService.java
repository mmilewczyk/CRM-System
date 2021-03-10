package com.agiklo.oracledatabase.service;

import com.agiklo.oracledatabase.entity.dto.ProductUnitsDTO;
import com.agiklo.oracledatabase.mapper.ProductUnitsMapper;
import com.agiklo.oracledatabase.repository.ProductUnitsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductUnitsService {

    private final ProductUnitsRepository productUnitsRepository;
    private final ProductUnitsMapper productUnitsMapper;

    @Transactional(readOnly = true)
    public List<ProductUnitsDTO> getAllProductUnits(){
        return productUnitsRepository.findAll()
                .stream()
                .map(productUnitsMapper::mapProductUnitsToDTO)
                .collect(Collectors.toList());
    }
}
