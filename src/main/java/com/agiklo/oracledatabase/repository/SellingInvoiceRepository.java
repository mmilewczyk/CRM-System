package com.agiklo.oracledatabase.repository;

import com.agiklo.oracledatabase.entity.SellingInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellingInvoiceRepository extends JpaRepository<SellingInvoice, Long> {
}
