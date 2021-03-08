package com.agiklo.oracledatabase.repository;

import com.agiklo.oracledatabase.entity.Absenteeism;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbsenteeismRepository extends JpaRepository<Absenteeism, Long> {
}
