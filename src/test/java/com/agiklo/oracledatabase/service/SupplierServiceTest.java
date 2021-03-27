package com.agiklo.oracledatabase.service;

import com.agiklo.oracledatabase.entity.Supplier;
import com.agiklo.oracledatabase.entity.dto.SupplierDTO;
import com.agiklo.oracledatabase.repository.SupplierRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("dev")
class SupplierServiceTest {

    @Autowired
    private SupplierService supplierService;
    @Autowired
    private SupplierRepository supplierRepository;

    @Test
    @Transactional
    void shouldGetSupplierById() {
        Supplier supplier = prepareSupplierToTest();
        supplierRepository.save(supplier);
        //when
        SupplierDTO supplierDTO = supplierService.getSupplierById(supplier.getSupplierId());
        //then
        assertThat(supplierDTO).isNotNull();
        assertThat(supplierDTO.getSupplierName()).isEqualTo("mitopharma");
        assertThat(supplierDTO.getActivityStatus()).isEqualTo("A");
    }

    Supplier prepareSupplierToTest(){
        Supplier newSupplier = new Supplier();
        newSupplier.setSupplierName("mitopharma");
        newSupplier.setActivityStatus("A");
        return newSupplier;
    }


}
