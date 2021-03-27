package com.agiklo.oracledatabase.service;

import com.agiklo.oracledatabase.entity.Product;
import com.agiklo.oracledatabase.entity.ProductType;
import com.agiklo.oracledatabase.entity.dto.ProductDTO;
import com.agiklo.oracledatabase.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("dev")
class ProductServiceTest {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    @Test
    @Transactional
    void shouldGetProductById() {
        Product product = prepareProductToTest();
        productRepository.save(product);
        //when
        ProductDTO productDTO = productService.getProductById(product.getId());
        //then
        assertThat(productDTO).isNotNull();
        assertThat(productDTO.getName()).isEqualTo("Tofu");
        assertThat(productDTO.getProductType()).isEqualTo("Protein");
        assertThat(productDTO.getSellingPrice()).isEqualTo(5.00);
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
