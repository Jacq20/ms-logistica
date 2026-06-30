package com.perfulandia.ms_logistica.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perfulandia.ms_logistica.model.EstadoOrden;
import com.perfulandia.ms_logistica.model.OrdenReabastecimiento;
import com.perfulandia.ms_logistica.repository.OrdenReabastecimientoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrdenReabastecimientoService {

    @Autowired
    private OrdenReabastecimientoRepository ordenRepository;

    public OrdenReabastecimiento crearOrden(OrdenReabastecimiento datos) {
        datos.setEstado(EstadoOrden.PENDIENTE);
        datos.setFechaOrden(LocalDateTime.now());
        return ordenRepository.save(datos);
    }

    public void aprobarOrden(Long idOrden) {
        OrdenReabastecimiento orden = ordenRepository.findById(idOrden)
                .orElseThrow(() -> new RuntimeException("No existe la orden de reabastecimiento"));

        orden.setEstado(EstadoOrden.APROBADA);
        ordenRepository.save(orden);
    }

    public void registrarRecepcion(Long idOrden) {
        OrdenReabastecimiento orden = ordenRepository.findById(idOrden)
                .orElseThrow(() -> new RuntimeException("No existe la orden de reabastecimiento"));

        orden.setEstado(EstadoOrden.RECIBIDA);
        ordenRepository.save(orden);
    }
}
