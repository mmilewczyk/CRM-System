package com.agiklo.oracledatabase.repository;

import com.agiklo.oracledatabase.entity.Customers;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customers, Long> {

    List<Customers> findCustomersByFirstnameContainingIgnoreCase(@RequestParam("firstname") String firstName, Pageable pageable);
}
