package com.perfulandia.ms_logistica.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perfulandia.ms_logistica.model.OrdenReabastecimiento;
import com.perfulandia.ms_logistica.service.OrdenReabastecimientoService;

@RestController
@RequestMapping("/api/v1/ordenes-reabastecimiento")
public class OrdenReabastecimientoController {

    @Autowired
    private OrdenReabastecimientoService ordenService;

    @PostMapping
    public OrdenReabastecimiento crearOrden(@RequestBody OrdenReabastecimiento orden) {
        return ordenService.crearOrden(orden);
    }

    @PutMapping("/{id}/aprobar")
    public ResponseEntity<Void> aprobarOrden(@PathVariable Long id) {
        ordenService.aprobarOrden(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/recepcion")
    public ResponseEntity<Void> registrarRecepcion(@PathVariable Long id) {
        ordenService.registrarRecepcion(id);
        return ResponseEntity.noContent().build();
    }
}
