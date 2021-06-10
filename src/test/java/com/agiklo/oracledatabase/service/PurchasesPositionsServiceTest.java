package com.agiklo.oracledatabase.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.agiklo.oracledatabase.mapper.PurchasesPositionsMapper;
import com.agiklo.oracledatabase.repository.PurchasesPositionsRepository;

import java.util.ArrayList;
import java.util.Optional;

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
import org.springframework.web.server.ResponseStatusException;

@ContextConfiguration(classes = {PurchasesPositionsService.class})
@ExtendWith(SpringExtension.class)
@Execution(ExecutionMode.CONCURRENT)
class PurchasesPositionsServiceTest {
    @MockBean
    private PurchasesPositionsMapper purchasesPositionsMapper;

    @MockBean
    private PurchasesPositionsRepository purchasesPositionsRepository;

    @Autowired
    private PurchasesPositionsService purchasesPositionsService;

    @Test
    void shouldGetEmptyListOfAllPurchasesPositions() {
        // when
        when(this.purchasesPositionsRepository.findAll((Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        // then
        assertTrue(this.purchasesPositionsService.getAllPurchasesPositions(null).isEmpty());
        verify(this.purchasesPositionsRepository).findAll((Pageable) any());
    }

    @Test
    void shouldGetEmptyListOfPurchasesById(){
        // when
        when(this.purchasesPositionsRepository.findById(any())).thenReturn(Optional.empty());

        // then
        assertThrows(ResponseStatusException.class, () -> purchasesPositionsService.getpurchasePositiontById(any()));
        verify(this.purchasesPositionsRepository).findById(any());
    }
}

