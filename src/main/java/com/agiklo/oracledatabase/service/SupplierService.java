package com.agiklo.oracledatabase.service;

import com.agiklo.oracledatabase.entity.Supplier;
import com.agiklo.oracledatabase.exports.ExportSuppliersToPDF;
import com.agiklo.oracledatabase.repository.SupplierRepository;
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
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public List<Supplier> getAllSuppliers(){
        return supplierRepository.findAll();
    }

    public Optional<Supplier> getSupplierById(Long id) {
        return supplierRepository.findById(id);
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

    public void exportToPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=suppliers_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<Supplier> supplierList = supplierRepository.findAll();

        ExportSuppliersToPDF exporter = new ExportSuppliersToPDF(supplierList);
        exporter.export(response);
    }
}
