package com.agiklo.oracledatabase.repository;

import com.agiklo.oracledatabase.entity.Departments;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentsRepository extends JpaRepository<Departments, Long> {

    List<Departments> getDepartmentsByDepartmentNameContainingIgnoreCase(String name, Pageable pageable);
}
