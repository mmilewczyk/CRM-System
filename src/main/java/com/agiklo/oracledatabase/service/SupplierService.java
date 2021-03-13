package com.agiklo.oracledatabase.service;

import com.agiklo.oracledatabase.entity.Supplier;
import com.agiklo.oracledatabase.entity.dto.SupplierDTO;
import com.agiklo.oracledatabase.exports.excel.ExportSuppliersToXLSX;
import com.agiklo.oracledatabase.exports.pdf.ExportSuppliersToPDF;
import com.agiklo.oracledatabase.mapper.SupplierMapper;
import com.agiklo.oracledatabase.repository.SupplierRepository;
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
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    @Transactional(readOnly = true)
    public List<SupplierDTO> getAllSuppliers(){
        return supplierRepository.findAll()
                .stream()
                .map(supplierMapper::mapSupplierToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SupplierDTO getSupplierById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Supplier cannot be found, the specified id does not exist"));
        return supplierMapper.mapSupplierToDTO(supplier);
    }

    public Supplier addNewSuppiler(Supplier supplier){
        return supplierRepository.save(supplier);
    }

    public void deleteSupplierById(Long id) throws NotFoundException {
        try {
            supplierRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("The specified id does not exist");
        }
    }

    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=suppliers_" + getCurrentDateTime() + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<Supplier> supplierList = supplierRepository.findAll();

        ExportSuppliersToXLSX exporter = new ExportSuppliersToXLSX(supplierList);
        exporter.export(response);
    }

    public void exportToPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=suppliers_" + getCurrentDateTime() + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<Supplier> supplierList = supplierRepository.findAll();

        ExportSuppliersToPDF exporter = new ExportSuppliersToPDF(supplierList);
        exporter.export(response);
    }

    private String getCurrentDateTime(){
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        return dateFormatter.format(new Date());
    }
}
