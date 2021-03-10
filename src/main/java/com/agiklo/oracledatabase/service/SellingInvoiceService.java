package com.agiklo.oracledatabase.service;

import com.agiklo.oracledatabase.entity.SellingInvoice;
import com.agiklo.oracledatabase.entity.dto.SellingInvoiceDTO;
import com.agiklo.oracledatabase.mapper.SellingInvoiceMapper;
import com.agiklo.oracledatabase.repository.SellingInvoiceRepository;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SellingInvoiceService {

    private final SellingInvoiceRepository sellingInvoiceRepository;
    private final SellingInvoiceMapper sellingInvoiceMapper;

    @Transactional(readOnly = true)
    public List<SellingInvoiceDTO> getAllSellingInvoices(){
        return sellingInvoiceRepository.findAll()
                .stream()
                .map(sellingInvoiceMapper::mapSellingInvoiceToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SellingInvoiceDTO getInvoiceById(Long id) {
        SellingInvoice sellingInvoice = sellingInvoiceRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Selling invoice cannot be found, the specified id does not exist"));
        return sellingInvoiceMapper.mapSellingInvoiceToDTO(sellingInvoice);
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
