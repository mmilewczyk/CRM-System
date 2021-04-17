package com.agiklo.oracledatabase.service;

import com.agiklo.oracledatabase.entity.Product;
import com.agiklo.oracledatabase.entity.dto.ProductDTO;
import com.agiklo.oracledatabase.exports.pdf.ExportProductsToPDF;
import com.agiklo.oracledatabase.exports.excel.ExportProductsToXLSX;
import com.agiklo.oracledatabase.mapper.ProductMapper;
import com.agiklo.oracledatabase.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Mateusz Milewczyk (agiklo)
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class ProductService implements CurrentTimeInterface{

    /**
     * were injected by the constructor using the lombok @AllArgsContrustor annotation
     */
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    /**
     * The method is to retrieve all products from the database and display them.
     *
     * After downloading all the data about the product,
     * the data is mapped to dto which will display only those needed
     * @return list of all products with specification of data in ProductDTO
     */
    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProducts(Pageable pageable){
        return productRepository.findAll(pageable)
                .stream()
                .map(productMapper::mapProductToDto)
                .collect(Collectors.toList());
    }

    /**
     * The method is to download a specific products from the database and display it.
     * After downloading all the data about the products,
     * the data is mapped to dto which will display only those needed
     *
     * @param id id of the product to be searched for
     * @throws ResponseStatusException if the id of the product you are looking for does not exist throws 404 status
     * @return detailed data about a specific product
     */
    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product cannot be found, the specified id does not exist"));
        return productMapper.mapProductToDto(product);
    }

    @Transactional
    public ProductDTO editProduct(Product product){
        Product editedProduct = productRepository.findById(product.getId())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product cannot be found"));
        editedProduct.setName(product.getName());
        editedProduct.setSellingPrice(product.getSellingPrice());
        editedProduct.setPurchasePrice(product.getPurchasePrice());
        editedProduct.setTaxRate(product.getTaxRate());
        return productMapper.mapProductToDto(editedProduct);
    }

    /**
     * The task of the method is to add a product to the database.
     * @param product requestbody of the customer to be saved
     * @return saving the product to the database
     */
    public Product addNewProduct(Product product) {
        return productRepository.save(product);
    }

    /**
     * Method deletes the selected product by id
     * @param id id of the product to be deleted
     * @throws ResponseStatusException if id of the product is incorrect throws 404 status with message
     */
    public void deleteProductById(Long id) {
        try{
            productRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The specified id does not exist");
        }
    }

    /**
     * The purpose of the method is to set the details of the
     * excel file that will be exported for download and then download it.
     * @param response response to determine the details of the file
     * @throws IOException if incorrect data is sent to the file
     */
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=products_" + getCurrentDateTime() + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<Product> productList = productRepository.findAll();

        ExportProductsToXLSX exporter = new ExportProductsToXLSX(productList);
        exporter.export(response);
    }

    /**
     * The purpose of the method is to set the details of the
     * pdf file that will be exported for download and then download it.
     * @param response response to determine the details of the file
     * @throws IOException if incorrect data is sent to the file
     */
    public void exportToPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=products_" + getCurrentDateTime() + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<Product> productList = productRepository.findAll();

        ExportProductsToPDF exporter = new ExportProductsToPDF(productList);
        exporter.export(response);
    }

    /**
     * The method is to retrieve products whose have the name specified by the user.
     * After downloading all the data about the product,
     * the data is mapped to dto which will display only those needed
     * @param name name of the product
     * @return details of specific products
     */
    @Transactional(readOnly = true)
    public List<ProductDTO> findAllByName(String name, Pageable pageable) {
        return productRepository.findProductsByNameContaining(name, pageable)
                .stream()
                .map(productMapper::mapProductToDto)
                .collect(Collectors.toList());
    }

    /**
     * The method is to retrieve products whose have the type specified by the user.
     * After downloading all the data about the product,
     * the data is mapped to dto which will display only those needed
     * @param productType type of the product
     * @return details of specific products
     */
    @Transactional(readOnly = true)
    public List<ProductDTO> findAllByProductType(String productType, Pageable pageable){
        return productRepository.findProductsByProductTypeFullNameContaining(productType, pageable)
                .stream()
                .map(productMapper::mapProductToDto)
                .collect(Collectors.toList());
    }
}
