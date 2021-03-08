package com.agiklo.oracledatabase.service;

import com.agiklo.oracledatabase.entity.PurchasesPositions;
import com.agiklo.oracledatabase.repository.PurchasesPositionsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PurchasesPositionsService {

    private final PurchasesPositionsRepository purchasesPositionsRepository;

    public List<PurchasesPositions> getAllPurchasesPositions(){
        return purchasesPositionsRepository.findAll();
    }

    public Optional<PurchasesPositions> getpurchasePositiontById(Long id) {
        return purchasesPositionsRepository.findById(id);
    }
}
