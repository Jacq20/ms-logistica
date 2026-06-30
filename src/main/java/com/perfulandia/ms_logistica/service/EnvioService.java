package com.perfulandia.ms_logistica.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perfulandia.ms_logistica.model.Envio;
import com.perfulandia.ms_logistica.model.EstadoEnvio;
import com.perfulandia.ms_logistica.repository.EnvioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class EnvioService {

    @Autowired
    private EnvioRepository envioRepository;

    public Envio crearEnvio(Envio datos) {
        datos.setEstado(EstadoEnvio.PREPARANDO);
        return envioRepository.save(datos);
    }

    public Envio actualizarEnvio(Long idEnvio, Envio datos) {
        Envio existente = envioRepository.findById(idEnvio)
                .orElseThrow(() -> new RuntimeException("No existe el envio"));

        existente.setIdVenta(datos.getIdVenta());
        existente.setIdSucursalOrigen(datos.getIdSucursalOrigen());
        existente.setDireccionDestino(datos.getDireccionDestino());
        existente.setFechaEstimada(datos.getFechaEstimada());
        existente.setRuta(datos.getRuta());

        return envioRepository.save(existente);
    }

    public Envio seguirEnvio(Long idEnvio) {
        return envioRepository.findById(idEnvio)
                .orElseThrow(() -> new RuntimeException("No existe el envio"));
    }

    public void actualizarEstadoEnvio(Long idEnvio, EstadoEnvio estado) {
        Envio existente = envioRepository.findById(idEnvio)
                .orElseThrow(() -> new RuntimeException("No existe el envio"));

        existente.setEstado(estado);
        envioRepository.save(existente);
    }
}
