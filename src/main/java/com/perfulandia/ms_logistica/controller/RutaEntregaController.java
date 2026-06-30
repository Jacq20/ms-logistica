package com.perfulandia.ms_logistica.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.perfulandia.ms_logistica.model.RutaEntrega;
import com.perfulandia.ms_logistica.service.RutaEntregaService;

@RestController
@RequestMapping("/api/v1/rutas")
public class RutaEntregaController {

    @Autowired
    private RutaEntregaService rutaEntregaService;

    @PostMapping("/optimizar")
    public RutaEntrega optimizarRuta(@RequestParam String origen, @RequestBody List<String> destinos) {
        return rutaEntregaService.optimizarRuta(origen, destinos);
    }
}
