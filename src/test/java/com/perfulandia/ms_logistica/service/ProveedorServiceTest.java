package com.perfulandia.ms_logistica.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.perfulandia.ms_logistica.model.EstadoProveedor;
import com.perfulandia.ms_logistica.model.Proveedor;
import com.perfulandia.ms_logistica.repository.ProveedorRepository;

@ExtendWith(MockitoExtension.class)
class ProveedorServiceTest {

    @Mock
    private ProveedorRepository proveedorRepository;

    @InjectMocks
    private ProveedorService proveedorService;

    @Test
    void testRegistrarProveedor() {
        // Given
        Proveedor nuevo = new Proveedor(null, "Distribuidora Aromas Ltda.", "76.123.456-7", "Juan Perez", null, "30 dias");
        Proveedor guardado = new Proveedor(1L, "Distribuidora Aromas Ltda.", "76.123.456-7", "Juan Perez", EstadoProveedor.ACTIVO, "30 dias");
        when(proveedorRepository.save(any(Proveedor.class))).thenReturn(guardado);

        // When
        Proveedor resultado = proveedorService.registrarProveedor(nuevo);

        // Then
        assertNotNull(resultado);
        assertEquals(EstadoProveedor.ACTIVO, resultado.getEstado());
        verify(proveedorRepository, times(1)).save(any(Proveedor.class));
    }

    @Test
    void testActualizarProveedor() {
        // Given
        Proveedor existente = new Proveedor(1L, "Distribuidora Aromas Ltda.", "76.123.456-7", "Juan Perez", EstadoProveedor.ACTIVO, "30 dias");
        Proveedor datosNuevos = new Proveedor(null, "Aromas SPA", "76.123.456-7", "Maria Lopez", null, "60 dias");
        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(proveedorRepository.save(existente)).thenReturn(existente);

        // When
        Proveedor resultado = proveedorService.actualizarProveedor(1L, datosNuevos);

        // Then
        assertEquals("Aromas SPA", resultado.getRazonSocial());
        assertEquals("60 dias", resultado.getCondicionesPago());
        verify(proveedorRepository, times(1)).save(existente);
    }

    @Test
    void testActualizarProveedorNoExistente() {
        // Given
        when(proveedorRepository.findById(99L)).thenReturn(Optional.empty());

        // When
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> proveedorService.actualizarProveedor(99L, new Proveedor()));

        // Then
        assertEquals("No existe el proveedor", exception.getMessage());
        verify(proveedorRepository, never()).save(any(Proveedor.class));
    }
}
