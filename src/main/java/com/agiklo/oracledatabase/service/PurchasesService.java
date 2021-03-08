package com.agiklo.oracledatabase.service;

import com.agiklo.oracledatabase.entity.Purchases;
import com.agiklo.oracledatabase.repository.PurchasesRepository;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PurchasesService {

    private final PurchasesRepository purchasesRepository;

    public List<Purchases> getAllPurchases(){
        return purchasesRepository.findAll();
    }

    public Optional<Purchases> getPurchaseById(Long id) {
        return purchasesRepository.findById(id);
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
