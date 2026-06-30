package com.perfulandia.ms_logistica.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perfulandia.ms_logistica.model.EstadoProveedor;
import com.perfulandia.ms_logistica.model.Proveedor;
import com.perfulandia.ms_logistica.repository.ProveedorRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    public Proveedor registrarProveedor(Proveedor datos) {
        datos.setEstado(EstadoProveedor.ACTIVO);
        return proveedorRepository.save(datos);
    }

    public Proveedor actualizarProveedor(Long idProveedor, Proveedor datos) {
        Proveedor existente = proveedorRepository.findById(idProveedor)
                .orElseThrow(() -> new RuntimeException("No existe el proveedor"));

        existente.setRazonSocial(datos.getRazonSocial());
        existente.setRut(datos.getRut());
        existente.setContacto(datos.getContacto());
        existente.setCondicionesPago(datos.getCondicionesPago());
        if (datos.getEstado() != null) {
            existente.setEstado(datos.getEstado());
        }

        return proveedorRepository.save(existente);
    }
}
