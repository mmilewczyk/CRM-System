package com.agiklo.oracledatabase.service;

import com.agiklo.oracledatabase.entity.Product;
import com.agiklo.oracledatabase.exports.ExportProductsToPDF;
import com.agiklo.oracledatabase.repository.ProductRepository;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id){
        return productRepository.findById(id);
    }

    public Product addNewProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProductById(Long id) throws NotFoundException {
        try{
            productRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("The specified id does not exist");
        }
    }

    public void exportToPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=products_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<Product> productList = productRepository.findAll();

        ExportProductsToPDF exporter = new ExportProductsToPDF(productList);
        exporter.export(response);
    }

    public List<Product> findAllByName(String name) throws NotFoundException {
        try {
            return productRepository.findProductsByNameContaining(name);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("The specified product does not exist");
        }
    }
}
