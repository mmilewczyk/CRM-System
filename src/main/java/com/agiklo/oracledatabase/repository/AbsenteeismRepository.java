package com.agiklo.oracledatabase.repository;

import com.agiklo.oracledatabase.entity.Absenteeism;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AbsenteeismRepository extends JpaRepository<Absenteeism, Long> {

    @EntityGraph(attributePaths = {"employee", "reasonOfAbsenteeismCode"})
    List<Absenteeism> findAllBy(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"employee", "reasonOfAbsenteeismCode"})
    Optional<Absenteeism> findById(Long aLong);
}
