package com.agiklo.oracledatabase.controller;

import com.agiklo.oracledatabase.entity.Product;
import com.agiklo.oracledatabase.entity.ProductType;
import com.agiklo.oracledatabase.entity.dto.ProductDTO;
import com.agiklo.oracledatabase.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("dev")
@AutoConfigureMockMvc
@Execution(ExecutionMode.CONCURRENT)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProductRepository productRepository;

    @Test
    @Disabled
    void shouldNoGetProductsAndReturnForbridden() throws Exception {
        //when
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/products"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();
        //then
        assertThat(mvcResult.getResponse().getErrorMessage()).isEqualTo("Access Denied");
    }

    @Test
    @Transactional
    @WithMockUser(username = "zofiabrzydal@agiklocrm.com", password = "123", authorities = "EMPLOYEE")
    void shouldGetProductById() throws Exception {
        //given
        Product fakeProduct = prepareProductToTest();
        productRepository.save(fakeProduct);
        //when
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/products/" + fakeProduct.getId()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        //then
        ProductDTO product = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ProductDTO.class);
        assertThat(product).isNotNull();
        assertThat(product.getName()).isEqualTo("Tofu");
        assertThat(product.getProductType()).isEqualTo("Protein");
    }

    @Test
    @WithMockUser(username = "zofiabrzydal@agiklocrm.com", password = "123", authorities = "EMPLOYEE")
    void shouldNoGetProductById() throws Exception {
        //given
        List<Product> products = productRepository.findAll();
        long fakeId;
        if (products.isEmpty()) {
            fakeId = 1;}
        else {
            fakeId = products.stream()
                .mapToLong(Product::getId)
                .max()
                .orElseThrow(NoSuchElementException::new);
        }
        fakeId++;
        //when
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/products/" + fakeId))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();
        //then
        assertThat(mvcResult.getResponse().getErrorMessage()).isEqualTo("Product cannot be found, the specified id does not exist");
    }

    Product prepareProductToTest(){
        Product product = new Product();
        product.setName("Tofu");
        product.setProductType(new ProductType("Protein", 0.5, 'K'));
        product.setPurchasePrice(3.99);
        product.setSellingPrice(5.00);
        return product;
    }


}
