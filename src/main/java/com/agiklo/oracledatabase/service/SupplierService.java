package com.agiklo.oracledatabase.service;

import com.agiklo.oracledatabase.entity.Supplier;
import com.agiklo.oracledatabase.entity.dto.SupplierDTO;
import com.agiklo.oracledatabase.exports.excel.ExportSuppliersToXLSX;
import com.agiklo.oracledatabase.exports.pdf.ExportSuppliersToPDF;
import com.agiklo.oracledatabase.mapper.SupplierMapper;
import com.agiklo.oracledatabase.repository.SupplierRepository;
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
public class SupplierService implements CurrentTimeInterface{

    /**
     * were injected by the constructor using the lombok @AllArgsContrustor annotation
     */
    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    /**
     * The method is to retrieve all suppliers from the database and display them.
     *
     * After downloading all the data about the supplier,
     * the data is mapped to dto which will display only those needed
     * @return list of all suppliers with specification of data in SupplierToDTO
     */
    @Transactional(readOnly = true)
    public List<SupplierDTO> getAllSuppliers(Pageable pageable){
        return supplierRepository.findAll(pageable)
                .stream()
                .map(supplierMapper::mapSupplierToDTO)
                .collect(Collectors.toList());
    }

    /**
     * The method is to download a specific supplier from the database and display it.
     * After downloading all the data about the supplier,
     * the data is mapped to dto which will display only those needed
     *
     * @param id id of the supplier to be searched for
     * @throws ResponseStatusException if the id of the supplier you are looking for does not exist throws 404 status
     * @return detailed data about a specific supplier
     */
    @Transactional(readOnly = true)
    public SupplierDTO getSupplierById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Supplier cannot be found, the specified id does not exist"));
        return supplierMapper.mapSupplierToDTO(supplier);
    }

    /**
     * The task of the method is to add a supplier to the database.
     * @param supplier requestbody of the supplier to be saved
     * @return saving the supplier to the database
     */
    public Supplier addNewSuppiler(Supplier supplier){
        return supplierRepository.save(supplier);
    }

    /**
     * Method deletes the selected supplier by id
     * @param id id of the supplier to be deleted
     * @throws ResponseStatusException if id of the supplier is incorrect throws 404 status with message
     */
    public void deleteSupplierById(Long id) {
        try {
            supplierRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The specified id does not exist");
        }
    }

    /**
     * Method enabling editing name and activicity status of the selected supplier.
     * @param supplier requestbody of the supplier to be edited
     * @return edited supplier
     */
    public SupplierDTO editSupplier(Supplier supplier){
        Supplier editedSupplier = supplierRepository.findById(supplier.getSupplierId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Supplier does not exist"));
        editedSupplier.setSupplierName(supplier.getSupplierName());
        editedSupplier.setActivityStatus(supplier.getActivityStatus());
        return supplierMapper.mapSupplierToDTO(editedSupplier);
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
        String headerValue = "attachment; filename=suppliers_" + getCurrentDateTime() + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<Supplier> supplierList = supplierRepository.findAll();

        ExportSuppliersToXLSX exporter = new ExportSuppliersToXLSX(supplierList);
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
        String headerValue = "attachment; filename=suppliers_" + getCurrentDateTime() + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<Supplier> supplierList = supplierRepository.findAll();

        ExportSuppliersToPDF exporter = new ExportSuppliersToPDF(supplierList);
        exporter.export(response);
    }
}
