package com.scada.repository;

import com.scada.entities.FaultAlgo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface FaultAlgoRepository extends JpaRepository<FaultAlgo, String> {
   Optional<FaultAlgo> findById(String id); // If "line" is the column name to match

	Optional<FaultAlgo> findByLine(String line);

}