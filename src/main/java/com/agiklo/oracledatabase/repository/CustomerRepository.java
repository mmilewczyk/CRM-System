package com.agiklo.oracledatabase.repository;

import com.agiklo.oracledatabase.entity.Customers;
import com.agiklo.oracledatabase.entity.dto.CustomerDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.Optional;
import java.util.Set;

@Repository
public interface CustomerRepository extends JpaRepository<Customers, Long> {

    Set<Customers> findCustomersByFirstnameLike(@RequestParam("firstname") String firstName);
}
