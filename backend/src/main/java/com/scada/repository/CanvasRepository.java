package com.scada.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scada.entities.CanvasData;

public interface CanvasRepository extends JpaRepository<CanvasData, Integer>
{

}
