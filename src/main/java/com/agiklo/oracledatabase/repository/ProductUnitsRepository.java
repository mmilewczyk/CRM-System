package com.agiklo.oracledatabase.repository;

import com.agiklo.oracledatabase.entity.ProductUnits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductUnitsRepository extends JpaRepository<ProductUnits, Long> {
}
