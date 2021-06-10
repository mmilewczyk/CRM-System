package com.agiklo.oracledatabase.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.agiklo.oracledatabase.entity.Supplier;
import com.agiklo.oracledatabase.entity.TypesOfTransport;
import com.agiklo.oracledatabase.entity.dto.SupplierDTO;
import com.agiklo.oracledatabase.enums.MODE_OF_TRANSPORT_CODE;
import com.agiklo.oracledatabase.mapper.SupplierMapper;
import com.agiklo.oracledatabase.repository.SupplierRepository;

import java.io.IOException;
import java.util.ArrayList;

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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

@ContextConfiguration(classes = {SupplierService.class})
@ExtendWith(SpringExtension.class)
@Execution(ExecutionMode.CONCURRENT)
class SupplierServiceTest {
    @MockBean
    private SupplierMapper supplierMapper;

    @MockBean
    private SupplierRepository supplierRepository;

    @Autowired
    private SupplierService supplierService;

    @Test
    void shouldGetEmptyListOfSuppliers() {
        // when
        when(this.supplierRepository.findAll((Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        // then
        assertTrue(this.supplierService.getAllSuppliers(null).isEmpty());
        verify(this.supplierRepository).findAll((Pageable) any());
    }

    @Test
    void shouldGetOneSuppliers() {
        // given
        Supplier supplier = createSupplier();
        ArrayList<Supplier> supplierList = new ArrayList();
        supplierList.add(supplier);
        PageImpl<Supplier> pageImpl = new PageImpl(supplierList);

        // when
        when(this.supplierRepository.findAll((Pageable) any())).thenReturn(pageImpl);
        when(this.supplierMapper.mapSupplierToDTO(any())).thenReturn(new SupplierDTO());

        // then
        assertEquals(1, this.supplierService.getAllSuppliers(null).size());
        verify(this.supplierMapper).mapSupplierToDTO(any());
        verify(this.supplierRepository).findAll((Pageable) any());
    }

    @Test
    void shouldGetSupplierById() {
        // given
        Supplier supplier = createSupplier();
        Optional<Supplier> ofResult = Optional.of(supplier);
        SupplierDTO supplierDTO = new SupplierDTO();

        // when
        when(this.supplierRepository.findById(any())).thenReturn(ofResult);
        when(this.supplierMapper.mapSupplierToDTO(any())).thenReturn(supplierDTO);

        // then
        assertSame(supplierDTO, this.supplierService.getSupplierById(123L));
        verify(this.supplierMapper).mapSupplierToDTO(any());
        verify(this.supplierRepository).findById(any());
    }

    @Test
    void shouldNotGetSupplierByIdAndThrowsException() {
        // when
        when(this.supplierRepository.findById(any())).thenReturn(Optional.empty());
        when(this.supplierMapper.mapSupplierToDTO(any())).thenReturn(new SupplierDTO());

        // then
        assertThrows(ResponseStatusException.class, () -> this.supplierService.getSupplierById(123L));
        verify(this.supplierRepository).findById(any());
    }

    @Test
    void shouldAddNewSuppiler() {
        // given
        Supplier supplier = createSupplier();

        // when
        when(this.supplierRepository.save(any())).thenReturn(supplier);

        // then
        assertSame(supplier, this.supplierService.addNewSuppiler(new Supplier()));
        verify(this.supplierRepository).save(any());
    }

    @Test
    void shouldDeleteSupplierById() {
        // when
        doNothing().when(this.supplierRepository).deleteById(any());
        this.supplierService.deleteSupplierById(123L);

        // then
        verify(this.supplierRepository).deleteById(any());
    }

    @Test
    void shouldNotDeleteSupplierByIdAndThrowsException() {
        // when
        doThrow(new EmptyResultDataAccessException(3)).when(this.supplierRepository).deleteById(any());

        // then
        assertThrows(ResponseStatusException.class, () -> this.supplierService.deleteSupplierById(123L));
        verify(this.supplierRepository).deleteById(any());
    }

    @Test
    void shouldEditSupplier() {
        // given
        Supplier supplier = createSupplier();
        SupplierDTO supplierDTO = new SupplierDTO();
        Optional<Supplier> ofResult = Optional.of(supplier);

        // when
        when(this.supplierRepository.findById(any())).thenReturn(ofResult);
        when(this.supplierMapper.mapSupplierToDTO(any())).thenReturn(supplierDTO);

        // then
        assertSame(supplierDTO, this.supplierService.editSupplier(new Supplier()));
        verify(this.supplierMapper).mapSupplierToDTO(any());
        verify(this.supplierRepository).findById(any());
    }

    @Test
    void shouldNotEditSupplierAndThrowsException() {
        // when
        when(this.supplierRepository.findById(any())).thenReturn(Optional.empty());
        when(this.supplierMapper.mapSupplierToDTO(any())).thenReturn(new SupplierDTO());

        // then
        assertThrows(ResponseStatusException.class, () -> this.supplierService.editSupplier(new Supplier()));
        verify(this.supplierRepository).findById(any());
    }

    @Test
    void shouldExportToPdf() throws IOException {
        // when
        when(this.supplierRepository.findAll()).thenReturn(new ArrayList<>());
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        this.supplierService.exportToPDF(mockHttpServletResponse);

        // then
        verify(this.supplierRepository).findAll();
        assertTrue(mockHttpServletResponse.isCommitted());
        assertNull(mockHttpServletResponse.getRedirectedUrl());
        assertEquals("application/pdf", mockHttpServletResponse.getContentType());
    }

    @Test
    void shouldExportToExcel() throws IOException {
        // when
        when(this.supplierRepository.findAll()).thenReturn(new ArrayList<>());
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        this.supplierService.exportToExcel(mockHttpServletResponse);

        // then
        verify(this.supplierRepository).findAll();
        assertTrue(mockHttpServletResponse.isCommitted());
        assertNull(mockHttpServletResponse.getRedirectedUrl());
        assertEquals("application/vnd.ms-excel", mockHttpServletResponse.getContentType());
    }

    private TypesOfTransport createTypeOfTransport(){
        return new TypesOfTransport(
                MODE_OF_TRANSPORT_CODE.VAN,
                "Van",
                10.0,
                10.0,
                10.0,
                10.0,
                400);
    }

    private Supplier createSupplier(){
        TypesOfTransport typesOfTransport = createTypeOfTransport();
        return new Supplier("IKIA", typesOfTransport, "Active");
    }
}

