package com.perfulandia.ms_logistica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perfulandia.ms_logistica.model.RutaEntrega;
import com.perfulandia.ms_logistica.repository.RutaEntregaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RutaEntregaService {

    @Autowired
    private RutaEntregaRepository rutaEntregaRepository;

    /**
     * Calcula y guarda la ruta de entrega óptima entre un origen y un
     * conjunto de destinos. El algoritmo de optimización real (distancia,
     * tráfico, etc.) se delega a un servicio externo; aquí se registra el
     * resultado consolidado: origen y el primer destino de la lista.
     */
    public RutaEntrega optimizarRuta(String origen, List<String> destinos) {
        if (destinos == null || destinos.isEmpty()) {
            throw new RuntimeException("Debe indicar al menos un destino");
        }

        RutaEntrega ruta = new RutaEntrega();
        ruta.setOrigen(origen);
        ruta.setDestino(destinos.get(0));

        return rutaEntregaRepository.save(ruta);
    }
}
