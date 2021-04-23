package com.agiklo.oracledatabase.repository;

import com.agiklo.oracledatabase.entity.Departments;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentsRepository extends JpaRepository<Departments, Long> {

    @EntityGraph(attributePaths = {"managers"})
    List<Departments> getDepartmentsByDepartmentNameContainingIgnoreCase(String name, Pageable pageable);

    @EntityGraph(attributePaths = {"managers"})
    List<Departments> findAllBy(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"managers"})
    Optional<Departments> findById(Long aLong);
}
