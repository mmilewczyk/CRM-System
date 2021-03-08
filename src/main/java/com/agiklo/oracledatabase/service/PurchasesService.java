package com.agiklo.oracledatabase.service;

import com.agiklo.oracledatabase.entity.Purchases;
import com.agiklo.oracledatabase.entity.dto.PurchasesDTO;
import com.agiklo.oracledatabase.mapper.PurchasesMapper;
import com.agiklo.oracledatabase.repository.PurchasesRepository;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PurchasesService {

    private final PurchasesRepository purchasesRepository;
    private final PurchasesMapper purchasesMapper;

    @Transactional(readOnly = true)
    public List<PurchasesDTO> getAllPurchases(){
        return purchasesRepository.findAll()
                .stream()
                .map(purchasesMapper::mapPurchasesToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PurchasesDTO getPurchaseById(Long id) {
        Purchases purchases = purchasesRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return purchasesMapper.mapPurchasesToDTO(purchases);
    }

    public Purchases addNewPurchase(Purchases purchase) {
        return purchasesRepository.save(purchase);
    }

    public void deletePurchaseById(Long id) throws NotFoundException {
        try{
            purchasesRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("The specified id does not exist");
        }
    }
}
