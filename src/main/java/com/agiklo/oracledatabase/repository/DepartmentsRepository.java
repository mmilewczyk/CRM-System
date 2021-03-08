package com.agiklo.oracledatabase.repository;

import com.agiklo.oracledatabase.entity.Departments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentsRepository extends JpaRepository<Departments, Long> {

}
