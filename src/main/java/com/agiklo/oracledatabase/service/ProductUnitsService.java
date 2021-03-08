package com.agiklo.oracledatabase.service;

import com.agiklo.oracledatabase.entity.ProductUnits;
import com.agiklo.oracledatabase.repository.ProductUnitsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductUnitsService {

    private final ProductUnitsRepository productUnitsRepository;

    public List<ProductUnits> getAllProductUnits(){
        return productUnitsRepository.findAll();
    }
}
