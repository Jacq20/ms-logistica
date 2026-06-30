package com.perfulandia.ms_logistica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.perfulandia.ms_logistica.model.OrdenReabastecimiento;

@Repository
public interface OrdenReabastecimientoRepository extends JpaRepository<OrdenReabastecimiento, Long> {
}
