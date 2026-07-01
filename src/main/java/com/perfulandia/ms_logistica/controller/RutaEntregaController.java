package com.perfulandia.ms_logistica.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perfulandia.ms_logistica.dto.RutaRequestDTO;
import com.perfulandia.ms_logistica.model.RutaEntrega;
import com.perfulandia.ms_logistica.service.RutaEntregaService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/v1/rutas")
public class RutaEntregaController {

    @Autowired
    private RutaEntregaService rutaEntregaService;

    @PostMapping("/optimizar")
    public RutaEntrega optimizarRuta(@RequestBody RutaRequestDTO request) {
        return rutaEntregaService.optimizarRuta(request.origen(), request.destinos());
    }
}