package com.agiklo.oracledatabase.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.agiklo.oracledatabase.entity.Customers;
import com.agiklo.oracledatabase.entity.Purchases;
import com.agiklo.oracledatabase.entity.SellingInvoice;
import com.agiklo.oracledatabase.entity.dto.PurchasesDTO;
import com.agiklo.oracledatabase.enums.CURRENCY;
import com.agiklo.oracledatabase.mapper.PurchasesMapper;
import com.agiklo.oracledatabase.repository.PurchasesRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

@ContextConfiguration(classes = {PurchasesService.class})
@ExtendWith(SpringExtension.class)
@Execution(ExecutionMode.CONCURRENT)
class PurchasesServiceTest {
    @MockBean
    private PurchasesMapper purchasesMapper;

    @MockBean
    private PurchasesRepository purchasesRepository;

    @Autowired
    private PurchasesService purchasesService;

    @Test
    void shouldGetEmptyListOfAllPurchases() {
        // when
        when(this.purchasesRepository.findAll((Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        // then
        assertTrue(this.purchasesService.getAllPurchases(null).isEmpty());
        verify(this.purchasesRepository).findAll((Pageable) any());
    }

    @Test
    void shouldGetOnePurchases() {
        // given
        Purchases purchases = createPurchase();
        ArrayList<Purchases> purchasesList = new ArrayList<>();
        purchasesList.add(purchases);
        PageImpl<Purchases> pageImpl = new PageImpl<>(purchasesList);

        // when
        when(this.purchasesRepository.findAll((Pageable) any())).thenReturn(pageImpl);
        when(this.purchasesMapper.mapPurchasesToDTO(any())).thenReturn(new PurchasesDTO());

        // then
        assertEquals(1, this.purchasesService.getAllPurchases(null).size());
        verify(this.purchasesMapper).mapPurchasesToDTO(any());
        verify(this.purchasesRepository).findAll((Pageable) any());
    }

    @Test
    void shouldGetPurchaseById() {
        // given
        Purchases purchases = createPurchase();
        PurchasesDTO purchasesDTO = new PurchasesDTO();
        Optional<Purchases> ofResult = Optional.of(purchases);

        // when
        when(this.purchasesRepository.findById(any())).thenReturn(ofResult);
        when(this.purchasesMapper.mapPurchasesToDTO(any())).thenReturn(purchasesDTO);

        // then
        assertSame(purchasesDTO, this.purchasesService.getPurchaseById(123L));
        verify(this.purchasesMapper).mapPurchasesToDTO(any());
        verify(this.purchasesRepository).findById(any());
    }

    @Test
    void shouldNotGetPurchaseByIdAndThrowsException() {
        // when
        when(this.purchasesRepository.findById(any())).thenReturn(Optional.<Purchases>empty());
        when(this.purchasesMapper.mapPurchasesToDTO(any())).thenReturn(new PurchasesDTO());

        // then
        assertThrows(ResponseStatusException.class, () -> this.purchasesService.getPurchaseById(123L));
        verify(this.purchasesRepository).findById(any());
    }

    @Test
    void shouldAddNewPurchase() {
        // given
        Purchases purchases = createPurchase();

        // when
        when(this.purchasesRepository.save(any())).thenReturn(purchases);

        // then
        assertSame(purchases, this.purchasesService.addNewPurchase(new Purchases()));
        verify(this.purchasesRepository).save(any());
    }

    @Test
    void shouldDeletePurchaseById() {
        // when
        doNothing().when(this.purchasesRepository).deleteById(any());
        this.purchasesService.deletePurchaseById(123L);

        // then
        verify(this.purchasesRepository).deleteById(any());
    }

    @Test
    void shouldNotDeletePurchaseAndThrowsException() {
        //when
        doThrow(new EmptyResultDataAccessException(3)).when(this.purchasesRepository).deleteById(any());

        // then
        assertThrows(ResponseStatusException.class, () -> this.purchasesService.deletePurchaseById(123L));
        verify(this.purchasesRepository).deleteById(any());
    }

    private Customers createCustomer(){
        return new Customers("Mateusz", "Milewczyk", "Pesel", "21-654", "Warsaw");
    }

    private SellingInvoice createSellingInvoice(){
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        Customers customer = createCustomer();
        return new SellingInvoice(
                Date.from(atStartOfDayResult.atZone(ZoneId.systemDefault()).toInstant()),
                customer,
                5000.0,
                6000.0,
                25.0,
                CURRENCY.PLN
        );
    }

    private Purchases createPurchase(){
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        Customers customer = createCustomer();
        SellingInvoice sellingInvoice = createSellingInvoice();
        return new Purchases(customer, 'N', 'Y', sellingInvoice, Date.from(atStartOfDayResult.atZone(ZoneId.systemDefault()).toInstant()));
    }
}

