package com.agiklo.oracledatabase.controller;

import com.agiklo.oracledatabase.entity.PurchasesPositions;
import com.agiklo.oracledatabase.entity.dto.PurchasesPositionsDTO;
import com.agiklo.oracledatabase.service.PurchasesPositionsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.agiklo.oracledatabase.controller.ApiMapping.PURCHASES_POSITIONS_REST_URL;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping(path = PURCHASES_POSITIONS_REST_URL)
@AllArgsConstructor
@CrossOrigin("*")
public class PurchasesPositionsController {

    private final PurchasesPositionsService purchasesPositionsService;

    @GetMapping
    public ResponseEntity<List<PurchasesPositionsDTO>> getAllPurchasesPositions(){
        return status(HttpStatus.OK).body(purchasesPositionsService.getAllPurchasesPositions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchasesPositionsDTO> getpurchasePositiontById(@PathVariable("id") Long id){
        return status(HttpStatus.OK).body(purchasesPositionsService.getpurchasePositiontById(id));
    }

}
