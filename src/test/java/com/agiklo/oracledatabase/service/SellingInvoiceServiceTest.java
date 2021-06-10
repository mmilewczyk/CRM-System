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
import com.agiklo.oracledatabase.entity.SellingInvoice;
import com.agiklo.oracledatabase.entity.dto.SellingInvoiceDTO;
import com.agiklo.oracledatabase.enums.CURRENCY;
import com.agiklo.oracledatabase.mapper.SellingInvoiceMapper;
import com.agiklo.oracledatabase.repository.SellingInvoiceRepository;

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

@ContextConfiguration(classes = {SellingInvoiceService.class})
@ExtendWith(SpringExtension.class)
@Execution(ExecutionMode.CONCURRENT)
class SellingInvoiceServiceTest {
    @MockBean
    private SellingInvoiceMapper sellingInvoiceMapper;

    @MockBean
    private SellingInvoiceRepository sellingInvoiceRepository;

    @Autowired
    private SellingInvoiceService sellingInvoiceService;

    @Test
    void shouldGetEmptyListOfAllSellingInvoices() {
        // when
        when(this.sellingInvoiceRepository.findAll((Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        // then
        assertTrue(this.sellingInvoiceService.getAllSellingInvoices(null).isEmpty());
        verify(this.sellingInvoiceRepository).findAll((Pageable) any());
    }

    @Test
    void shouldGetAllSellingInvoices() {
        // given
        SellingInvoice sellingInvoice = createSellingInvoice();
        ArrayList<SellingInvoice> sellingInvoiceList = new ArrayList<>();
        sellingInvoiceList.add(sellingInvoice);
        PageImpl<SellingInvoice> pageImpl = new PageImpl<>(sellingInvoiceList);

        // when
        when(this.sellingInvoiceRepository.findAll((Pageable) any())).thenReturn(pageImpl);
        when(this.sellingInvoiceMapper.mapSellingInvoiceToDTO(any())).thenReturn(new SellingInvoiceDTO());

        // then
        assertEquals(1, this.sellingInvoiceService.getAllSellingInvoices(null).size());
        verify(this.sellingInvoiceMapper).mapSellingInvoiceToDTO(any());
        verify(this.sellingInvoiceRepository).findAll((Pageable) any());
    }

    @Test
    void shouldGetInvoiceById() {
        // given
        SellingInvoice sellingInvoice = createSellingInvoice();
        SellingInvoiceDTO sellingInvoiceDTO = new SellingInvoiceDTO();
        Optional<SellingInvoice> ofResult = Optional.of(sellingInvoice);

        // when
        when(this.sellingInvoiceRepository.findById(any())).thenReturn(ofResult);
        when(this.sellingInvoiceMapper.mapSellingInvoiceToDTO(any())).thenReturn(sellingInvoiceDTO);

        // then
        assertSame(sellingInvoiceDTO, this.sellingInvoiceService.getInvoiceById(123L));
        verify(this.sellingInvoiceMapper).mapSellingInvoiceToDTO(any());
        verify(this.sellingInvoiceRepository).findById(any());
    }

    @Test
    void shouldNotGetInvoiceByIdAndThrowsException() {
        // when
        when(this.sellingInvoiceRepository.findById(any())).thenReturn(Optional.empty());
        when(this.sellingInvoiceMapper.mapSellingInvoiceToDTO(any())).thenReturn(new SellingInvoiceDTO());

        // then
        assertThrows(ResponseStatusException.class, () -> this.sellingInvoiceService.getInvoiceById(123L));
        verify(this.sellingInvoiceRepository).findById(any());
    }

    @Test
    void shouldAddNewInvoice() {
        // given
        SellingInvoice sellingInvoice = createSellingInvoice();

        // when
        when(this.sellingInvoiceRepository.save(any())).thenReturn(sellingInvoice);

        // then
        assertSame(sellingInvoice, this.sellingInvoiceService.addNewInvoice(new SellingInvoice()));
        verify(this.sellingInvoiceRepository).save(any());
    }

    @Test
    void shouldDeleteInvoiceById() {
        // when
        doNothing().when(this.sellingInvoiceRepository).deleteById(any());
        this.sellingInvoiceService.deleteInvoiceById(123L);

        // then
        verify(this.sellingInvoiceRepository).deleteById(any());
    }

    @Test
    void shouldNotDeleteInvoiceByIdAndThrowsException() {
        // when
        doThrow(new EmptyResultDataAccessException(3)).when(this.sellingInvoiceRepository).deleteById(any());

        // then
        assertThrows(ResponseStatusException.class, () -> this.sellingInvoiceService.deleteInvoiceById(123L));
        verify(this.sellingInvoiceRepository).deleteById(any());
    }

    @Test
    void shouldEditSellingInvoice() {
        // given
        SellingInvoice sellingInvoice = createSellingInvoice();
        SellingInvoiceDTO sellingInvoiceDTO = new SellingInvoiceDTO();
        Optional<SellingInvoice> ofResult = Optional.of(sellingInvoice);

        // when
        when(this.sellingInvoiceRepository.findById(any())).thenReturn(ofResult);
        when(this.sellingInvoiceMapper.mapSellingInvoiceToDTO(any())).thenReturn(sellingInvoiceDTO);

        // then
        assertSame(sellingInvoiceDTO, this.sellingInvoiceService.editSellingInvoice(new SellingInvoice()));
        verify(this.sellingInvoiceMapper).mapSellingInvoiceToDTO(any());
        verify(this.sellingInvoiceRepository).findById(any());
    }

    @Test
    void shouldNotEditSellingInvoiceAndThrowsException() {
        // when
        when(this.sellingInvoiceRepository.findById(any())).thenReturn(Optional.empty());
        when(this.sellingInvoiceMapper.mapSellingInvoiceToDTO(any())).thenReturn(new SellingInvoiceDTO());

        // then
        assertThrows(ResponseStatusException.class,
                () -> this.sellingInvoiceService.editSellingInvoice(new SellingInvoice()));
        verify(this.sellingInvoiceRepository).findById(any());
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
}

