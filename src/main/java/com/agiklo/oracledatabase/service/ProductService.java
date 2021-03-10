package com.agiklo.oracledatabase.service;

import com.agiklo.oracledatabase.entity.Product;
import com.agiklo.oracledatabase.entity.dto.ProductDTO;
import com.agiklo.oracledatabase.exports.ExportProductsToPDF;
import com.agiklo.oracledatabase.mapper.ProductMapper;
import com.agiklo.oracledatabase.repository.ProductRepository;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProducts(){
        return productRepository.findAll()
                .stream()
                .map(productMapper::mapProductToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product cannot be found, the specified id does not exist"));
        return productMapper.mapProductToDto(product);
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

    @Transactional(readOnly = true)
    public List<ProductDTO> findAllByName(String name) {
        return productRepository.findProductsByNameContaining(name)
                .stream()
                .map(productMapper::mapProductToDto)
                .collect(Collectors.toList());
    }
}
