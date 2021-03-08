package com.agiklo.oracledatabase.repository;

import com.agiklo.oracledatabase.enums.MODE_OF_TRANSPORT_CODE;
import com.agiklo.oracledatabase.entity.TypesOfTransport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypesOfTransportRepository extends JpaRepository<TypesOfTransport, MODE_OF_TRANSPORT_CODE> {
}
