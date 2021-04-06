package com.agiklo.oracledatabase.controller;

import com.agiklo.oracledatabase.entity.dto.PurchasesPositionsDTO;
import com.agiklo.oracledatabase.service.PurchasesPositionsService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.agiklo.oracledatabase.controller.ApiMapping.PURCHASES_POSITIONS_REST_URL;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping(path = PURCHASES_POSITIONS_REST_URL)
@AllArgsConstructor
@CrossOrigin("*")
public class PurchasesPositionsController {

    private final PurchasesPositionsService purchasesPositionsService;

    @GetMapping
    @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public ResponseEntity<List<PurchasesPositionsDTO>> getAllPurchasesPositions(Pageable pageable){
        return status(HttpStatus.OK).body(purchasesPositionsService.getAllPurchasesPositions(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public ResponseEntity<PurchasesPositionsDTO> getpurchasePositiontById(@PathVariable("id") Long id){
        return status(HttpStatus.OK).body(purchasesPositionsService.getpurchasePositiontById(id));
    }

}
