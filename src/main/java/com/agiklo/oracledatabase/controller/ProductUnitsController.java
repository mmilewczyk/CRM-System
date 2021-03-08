package com.agiklo.oracledatabase.controller;

import com.agiklo.oracledatabase.entity.ProductUnits;
import com.agiklo.oracledatabase.entity.dto.ProductUnitsDTO;
import com.agiklo.oracledatabase.service.ProductUnitsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.agiklo.oracledatabase.controller.ApiMapping.PRODUCT_UNITS_REST_URL;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping(produces="application/json", path = PRODUCT_UNITS_REST_URL)
@AllArgsConstructor
@CrossOrigin(origins="*")
public class ProductUnitsController {
    private final ProductUnitsService productUnitsService;

    @GetMapping
    public ResponseEntity<List<ProductUnitsDTO>> getAllProductUnits(){
        return status(HttpStatus.OK).body(productUnitsService.getAllProductUnits());
    }
}
