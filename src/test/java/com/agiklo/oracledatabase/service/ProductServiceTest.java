package com.agiklo.oracledatabase.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.agiklo.oracledatabase.entity.Product;
import com.agiklo.oracledatabase.entity.ProductType;
import com.agiklo.oracledatabase.entity.dto.ProductDTO;
import com.agiklo.oracledatabase.enums.UNITS_OF_MEASURE;
import com.agiklo.oracledatabase.mapper.ProductMapper;
import com.agiklo.oracledatabase.repository.ProductRepository;

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

@ContextConfiguration(classes = {ProductService.class})
@ExtendWith(SpringExtension.class)
@Execution(ExecutionMode.CONCURRENT)
class ProductServiceTest {
    @MockBean
    private ProductMapper productMapper;

    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Test
    void shouldGetAllEmptyListOfProducts() {
        when(this.productRepository.findAll((Pageable) any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        assertTrue(this.productService.getAllProducts(null).isEmpty());
        verify(this.productRepository).findAll((Pageable) any());
    }

    @Test
    void shouldGetAllProducts() {
        // given
        ProductType productType = new ProductType("Tofu", 5.0, 'L');
        Product product = new Product("Tofu", productType, 6.0, 4.0, 25.0, UNITS_OF_MEASURE.SZT);
        ArrayList<Product> productList = new ArrayList<>();
        productList.add(product);
        PageImpl<Product> pageImpl = new PageImpl<>(productList);

        // when
        when(this.productRepository.findAll((Pageable) any())).thenReturn(pageImpl);
        when(this.productMapper.mapProductToDto(any())).thenReturn(new ProductDTO());

        // then
        assertEquals(1, this.productService.getAllProducts(null).size());
        verify(this.productMapper).mapProductToDto(any());
        verify(this.productRepository).findAll((Pageable) any());
    }

    @Test
    void shouldGetProductById() {
        // given
        ProductType productType = new ProductType("Tofu", 5.0, 'L');
        Product product = new Product("Tofu", productType, 6.0, 4.0, 25.0, UNITS_OF_MEASURE.SZT);
        ProductDTO productDTO = new ProductDTO();
        Optional<Product> ofResult = Optional.of(product);

        // when
        when(this.productRepository.findById(any())).thenReturn(ofResult);
        when(this.productMapper.mapProductToDto(any())).thenReturn(productDTO);

        // then
        assertSame(productDTO, this.productService.getProductById(123L));
        verify(this.productMapper).mapProductToDto(any());
        verify(this.productRepository).findById(any());
    }

    @Test
    void shouldNotGetProductByIdAndThrowsException() {
        // when
        when(this.productRepository.findById(any())).thenReturn(Optional.empty());
        when(this.productMapper.mapProductToDto(any())).thenReturn(new ProductDTO());

        // then
        assertThrows(ResponseStatusException.class, () -> this.productService.getProductById(123L));
        verify(this.productRepository).findById(any());
    }

    @Test
    void shouldEditProduct() {
        // given
        ProductType productType = new ProductType("Tofu", 5.0, 'L');
        Product product = new Product("Tofu", productType, 6.0, 4.0, 25.0, UNITS_OF_MEASURE.SZT);
        ProductDTO productDTO = new ProductDTO();
        Optional<Product> ofResult = Optional.of(product);

        // when
        when(this.productRepository.findById(any())).thenReturn(ofResult);
        when(this.productMapper.mapProductToDto(any())).thenReturn(productDTO);

        // then
        assertSame(productDTO, this.productService.editProduct(new Product()));
        verify(this.productMapper).mapProductToDto(any());
        verify(this.productRepository).findById(any());
    }

    @Test
    void shouldNotEditProductAndThrowsException() {
        // when
        when(this.productRepository.findById(any())).thenReturn(Optional.empty());
        when(this.productMapper.mapProductToDto(any())).thenReturn(new ProductDTO());

        // then
        assertThrows(ResponseStatusException.class, () -> this.productService.editProduct(new Product()));
        verify(this.productRepository).findById(any());
    }

    @Test
    void shouldAddNewProduct() {
        // given
        ProductType productType = new ProductType("Tofu", 5.0, 'L');
        Product product = new Product("Tofu", productType, 6.0, 4.0, 25.0, UNITS_OF_MEASURE.SZT);

        // when
        when(this.productRepository.save(any())).thenReturn(product);

        // then
        assertSame(product, this.productService.addNewProduct(new Product()));
        verify(this.productRepository).save(any());
    }

    @Test
    void shouldDeleteProductById() {
        doNothing().when(this.productRepository).deleteById(any());
        this.productService.deleteProductById(123L);
        verify(this.productRepository).deleteById(any());
    }

    @Test
    void shouldNotDeleteProductByIdAndThrowsException() {
        doThrow(new EmptyResultDataAccessException(3)).when(this.productRepository).deleteById(any());
        assertThrows(ResponseStatusException.class, () -> this.productService.deleteProductById(123L));
        verify(this.productRepository).deleteById(any());
    }

    @Test
    void shouldExportToExcel() throws IOException {
        when(this.productRepository.findAll()).thenReturn(new ArrayList<>());
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        this.productService.exportToExcel(mockHttpServletResponse);
        verify(this.productRepository).findAll();
        assertTrue(mockHttpServletResponse.isCommitted());
        assertNull(mockHttpServletResponse.getRedirectedUrl());
        assertEquals("application/vnd.ms-excel", mockHttpServletResponse.getContentType());
    }

    @Test
    void shouldExportToPdf() throws IOException {
        when(this.productRepository.findAll()).thenReturn(new ArrayList<>());
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        this.productService.exportToPDF(mockHttpServletResponse);
        verify(this.productRepository).findAll();
        assertTrue(mockHttpServletResponse.isCommitted());
        assertNull(mockHttpServletResponse.getRedirectedUrl());
        assertEquals("application/pdf", mockHttpServletResponse.getContentType());
    }

    @Test
    void shouldFindEmptyListWhenUseFindAllProductsByName() {
        // when
        when(this.productRepository.findProductsByNameContaining(anyString(), any()))
                .thenReturn(new ArrayList<>());

        // then
        assertTrue(this.productService.findAllByName("Tofu", null).isEmpty());
        verify(this.productRepository).findProductsByNameContaining(anyString(), any());
    }

    @Test
    void shouldFindAllProductsByName() {
        // given
        ProductType productType = new ProductType("Tofu", 5.0, 'L');
        Product product = new Product("Tofu", productType, 6.0, 4.0, 25.0, UNITS_OF_MEASURE.SZT);
        ArrayList<Product> productList = new ArrayList<>();
        productList.add(product);

        // when
        when(this.productRepository.findProductsByNameContaining(anyString(), any())).thenReturn(productList);
        when(this.productMapper.mapProductToDto(any())).thenReturn(new ProductDTO());

        // then
        assertEquals(1, this.productService.findAllByName("Tofu", null).size());
        verify(this.productMapper).mapProductToDto(any());
        verify(this.productRepository).findProductsByNameContaining(anyString(), any());
    }

    @Test
    void shouldFindEmptylistOfAllByProductType() {
        // when
        when(this.productRepository.findProductsByProductTypeFullNameContaining(anyString(), any()))
                .thenReturn(new ArrayList<>());

        // then
        assertTrue(this.productService.findAllByProductType("Product Type", null).isEmpty());
        verify(this.productRepository).findProductsByProductTypeFullNameContaining(anyString(), any());
    }

    @Test
    void shouldFindAllByProductType() {
        // given
        ProductType productType = new ProductType("Tofu", 5.0, 'L');
        Product product = new Product("Tofu", productType, 6.0, 4.0, 25.0, UNITS_OF_MEASURE.SZT);
        ArrayList<Product> productList = new ArrayList<>();
        productList.add(product);

        // when
        when(this.productRepository.findProductsByProductTypeFullNameContaining(anyString(), any()))
                .thenReturn(productList);
        when(this.productMapper.mapProductToDto(any())).thenReturn(new ProductDTO());

        // then
        assertEquals(1, this.productService.findAllByProductType("Product Type", null).size());
        verify(this.productMapper).mapProductToDto(any());
        verify(this.productRepository).findProductsByProductTypeFullNameContaining(anyString(), any());
    }
}

