package com.agiklo.oracledatabase.service;

import com.agiklo.oracledatabase.entity.PurchasesPositions;
import com.agiklo.oracledatabase.entity.dto.PurchasesPositionsDTO;
import com.agiklo.oracledatabase.mapper.PurchasesPositionsMapper;
import com.agiklo.oracledatabase.repository.PurchasesPositionsRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Mateusz Milewczyk (agiklo)
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class PurchasesPositionsService {

    /**
     * were injected by the constructor using the lombok @AllArgsContrustor annotation
     */
    private final PurchasesPositionsRepository purchasesPositionsRepository;
    private final PurchasesPositionsMapper purchasesPositionsMapper;

    /**
     * The method is to retrieve all purchases positions from the database and display them.
     *
     * After downloading all the data about the purchases positions,
     * the data is mapped to dto which will display only those needed
     * @return list of all purchases positions with specification of data in PurchasesPositionsToDTO
     */
    @Transactional(readOnly = true)
    public List<PurchasesPositionsDTO> getAllPurchasesPositions(Pageable pageable){
        return purchasesPositionsRepository.findAll(pageable)
                .stream()
                .map(purchasesPositionsMapper::mapPurchasesPositionsToDTO)
                .collect(Collectors.toList());
    }

    /**
     * The method is to download a specific purchases position from the database and display it.
     * After downloading all the data about the purchases position,
     * the data is mapped to dto which will display only those needed
     *
     * @param id id of the purchases position to be searched for
     * @throws ResponseStatusException if the id of the purchases position you are looking for does not exist throws 404 status
     * @return detailed data about a specific purchases position
     */
    @Transactional(readOnly = true)
    public PurchasesPositionsDTO getpurchasePositiontById(Long id) {
        PurchasesPositions purchasesPositions = purchasesPositionsRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Purchase position cannot be found, the specified id does not exist"));
        return purchasesPositionsMapper.mapPurchasesPositionsToDTO(purchasesPositions);
    }
}
