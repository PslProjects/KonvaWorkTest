package com.scada.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scada.entities.FaultSession;

public interface FaultSessionRepository extends JpaRepository<FaultSession, Long> {}