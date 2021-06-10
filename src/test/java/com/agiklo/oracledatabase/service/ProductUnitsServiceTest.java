package com.agiklo.oracledatabase.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.agiklo.oracledatabase.entity.Product;
import com.agiklo.oracledatabase.entity.ProductType;
import com.agiklo.oracledatabase.entity.ProductUnits;
import com.agiklo.oracledatabase.entity.dto.ProductUnitsDTO;
import com.agiklo.oracledatabase.enums.UNITS_OF_MEASURE;
import com.agiklo.oracledatabase.mapper.ProductUnitsMapper;
import com.agiklo.oracledatabase.repository.ProductUnitsRepository;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ProductUnitsService.class})
@ExtendWith(SpringExtension.class)
@Execution(ExecutionMode.CONCURRENT)
class ProductUnitsServiceTest {
    @MockBean
    private ProductUnitsMapper productUnitsMapper;

    @MockBean
    private ProductUnitsRepository productUnitsRepository;

    @Autowired
    private ProductUnitsService productUnitsService;

    @Test
    void shouldGetEmptyListOfAllProductUnits() {
        // when
        when(this.productUnitsRepository.findAll((Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        // then
        assertTrue(this.productUnitsService.getAllProductUnits(null).isEmpty());
        verify(this.productUnitsRepository).findAll((Pageable) any());
    }

    @Test
    void shouldGetOneProductUnits() {
        // given
        ProductType productType = new ProductType("Protein", 12.00, 'L');
        Product product = new Product("Tofu", productType, 6.0, 4.0, 25.0, UNITS_OF_MEASURE.SZT);
        ProductUnits productUnits = new ProductUnits(product, UNITS_OF_MEASURE.SZT, "Gram", 10.0);
        ArrayList<ProductUnits> productUnitsList = new ArrayList<>();
        productUnitsList.add(productUnits);
        PageImpl<ProductUnits> pageImpl = new PageImpl<>(productUnitsList);

        // when
        when(this.productUnitsRepository.findAll((Pageable) any())).thenReturn(pageImpl);
        when(this.productUnitsMapper.mapProductUnitsToDTO(any())).thenReturn(new ProductUnitsDTO());

        // then
        assertEquals(1, this.productUnitsService.getAllProductUnits(null).size());
        verify(this.productUnitsMapper).mapProductUnitsToDTO(any());
        verify(this.productUnitsRepository).findAll((Pageable) any());
    }
}

