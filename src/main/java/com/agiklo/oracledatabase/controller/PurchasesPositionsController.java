package com.agiklo.oracledatabase.controller;

import com.agiklo.oracledatabase.entity.PurchasesPositions;
import com.agiklo.oracledatabase.service.PurchasesPositionsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.agiklo.oracledatabase.controller.ApiMapping.PURCHASES_POSITIONS_REST_URL;

@RestController
@RequestMapping(path = PURCHASES_POSITIONS_REST_URL)
@AllArgsConstructor
@CrossOrigin("*")
public class PurchasesPositionsController {

    private final PurchasesPositionsService purchasesPositionsService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PurchasesPositions> getAllPurchasesPositions(){
        return purchasesPositionsService.getAllPurchasesPositions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchasesPositions> getpurchasePositiontById(@PathVariable("id") Long id){
        Optional<PurchasesPositions> optionalPurchasesPositions = purchasesPositionsService.getpurchasePositiontById(id);
        return optionalPurchasesPositions
                .map(purchasesPositions -> new ResponseEntity<>(purchasesPositions, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

}
