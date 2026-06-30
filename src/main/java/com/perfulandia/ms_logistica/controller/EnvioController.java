package com.perfulandia.ms_logistica.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perfulandia.ms_logistica.model.Envio;
import com.perfulandia.ms_logistica.model.EstadoEnvio;
import com.perfulandia.ms_logistica.service.EnvioService;

@RestController
@RequestMapping("/api/v1/envios")
public class EnvioController {

    @Autowired
    private EnvioService envioService;

    @PostMapping
    public Envio crearEnvio(@RequestBody Envio envio) {
        return envioService.crearEnvio(envio);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Envio> actualizarEnvio(@PathVariable Long id, @RequestBody Envio datos) {
        return ResponseEntity.ok(envioService.actualizarEnvio(id, datos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Envio> seguirEnvio(@PathVariable Long id) {
        return ResponseEntity.ok(envioService.seguirEnvio(id));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Void> actualizarEstadoEnvio(@PathVariable Long id, @RequestBody EstadoEnvio estado) {
        envioService.actualizarEstadoEnvio(id, estado);
        return ResponseEntity.noContent().build();
    }
}
