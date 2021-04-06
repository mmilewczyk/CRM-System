package com.agiklo.oracledatabase.service;

import com.agiklo.oracledatabase.entity.dto.ProductUnitsDTO;
import com.agiklo.oracledatabase.mapper.ProductUnitsMapper;
import com.agiklo.oracledatabase.repository.ProductUnitsRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Mateusz Milewczyk (agiklo)
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class ProductUnitsService {

    /**
     * were injected by the constructor using the lombok @AllArgsContrustor annotation
     */
    private final ProductUnitsRepository productUnitsRepository;
    private final ProductUnitsMapper productUnitsMapper;

    /**
     * The method is to retrieve all ProductUnits from the database and display them.
     *
     * After downloading all the data about the ProductUnits,
     * the data is mapped to dto which will display only those needed
     * @return list of all ProductUnits with specification of data in ProductUnitsDTO
     */
    @Transactional(readOnly = true)
    public List<ProductUnitsDTO> getAllProductUnits(Pageable pageable){
        return productUnitsRepository.findAll(pageable)
                .stream()
                .map(productUnitsMapper::mapProductUnitsToDTO)
                .collect(Collectors.toList());
    }
}
