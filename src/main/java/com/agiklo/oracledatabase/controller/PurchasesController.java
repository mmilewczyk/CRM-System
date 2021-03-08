package com.agiklo.oracledatabase.controller;

import com.agiklo.oracledatabase.entity.Purchases;
import com.agiklo.oracledatabase.service.PurchasesService;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.agiklo.oracledatabase.controller.ApiMapping.PURCHASES_REST_URL;

@RestController
@RequestMapping(produces="application/json", path = PURCHASES_REST_URL)
@AllArgsConstructor
@CrossOrigin(origins="*")
public class PurchasesController {

    private final PurchasesService purchasesService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Purchases> getAllPurchases(){
        return purchasesService.getAllPurchases();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Purchases> getPurchaseById(@PathVariable("id") Long id){
        Optional<Purchases> optionalPurchases = purchasesService.getPurchaseById(id);
        return optionalPurchases
                .map(purchases -> new ResponseEntity<>(purchases, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(consumes="application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Purchases postNewPurchase(@RequestBody Purchases purchase) {
        return purchasesService.addNewPurchase(purchase);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePurchaseById(@PathVariable("id") Long id) throws NotFoundException {
       purchasesService.deletePurchaseById(id);
    }
}
