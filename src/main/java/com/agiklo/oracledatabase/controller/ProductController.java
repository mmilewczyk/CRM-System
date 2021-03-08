package com.agiklo.oracledatabase.controller;

import com.agiklo.oracledatabase.entity.Product;
import com.agiklo.oracledatabase.service.ProductService;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.agiklo.oracledatabase.controller.ApiMapping.PRODUCTS_REST_URL;

@RestController
@RequestMapping(produces="application/json", path = PRODUCTS_REST_URL)
@AllArgsConstructor
@CrossOrigin(origins="*")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long id){
        Optional<Product> optionalProducts = productService.getProductById(id);
        return optionalProducts
                .map(product -> new ResponseEntity<>(product, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<Product> findAllByName(@RequestParam("name") String name) throws NotFoundException {
        return productService.findAllByName(name);
    }

    @PostMapping(consumes="application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Product postNewProduct(@RequestBody Product product) {
        return productService.addNewProduct(product);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductById(@PathVariable("id") Long id) throws NotFoundException {
        productService.deleteProductById(id);
    }

    @GetMapping("/export/pdf")
    @ResponseStatus(HttpStatus.CREATED)
    public void exportToPDF(HttpServletResponse response) throws IOException {
        productService.exportToPDF(response);
    }
}
