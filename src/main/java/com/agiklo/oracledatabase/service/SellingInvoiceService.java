package com.agiklo.oracledatabase.service;

import com.agiklo.oracledatabase.entity.SellingInvoice;
import com.agiklo.oracledatabase.repository.SellingInvoiceRepository;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SellingInvoiceService {

    private final SellingInvoiceRepository sellingInvoiceRepository;

    public List<SellingInvoice> getAllSellingInvoices(){
        return sellingInvoiceRepository.findAll();
    }

    public Optional<SellingInvoice> getInvoiceById(Long id) {
        return sellingInvoiceRepository.findById(id);
    }

    public SellingInvoice addNewInvoice(SellingInvoice sellingInvoice) {
        return sellingInvoiceRepository.save(sellingInvoice);
    }

    public void deleteInvoiceById(Long id) throws NotFoundException {
        try{
            sellingInvoiceRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("The specified id does not exist");
        }
    }
}
