package com.scada.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scada.entities.FaultAction;

public interface FaultActionRepository extends JpaRepository<FaultAction, Long> {}