package com.agiklo.oracledatabase.repository;

import com.agiklo.oracledatabase.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@Transactional(readOnly = true)
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE Employee a " +
            "SET a.isEnabled = TRUE WHERE a.email = ?1")
    int enableUser(String email);

    Set<Employee> findUserByFirstNameLike(@RequestParam("firstname") String firstName);

}
