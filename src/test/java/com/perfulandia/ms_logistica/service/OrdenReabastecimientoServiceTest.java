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

import com.perfulandia.ms_logistica.model.EstadoOrden;
import com.perfulandia.ms_logistica.model.OrdenReabastecimiento;
import com.perfulandia.ms_logistica.repository.OrdenReabastecimientoRepository;

@ExtendWith(MockitoExtension.class)
class OrdenReabastecimientoServiceTest {

    @Mock
    private OrdenReabastecimientoRepository ordenRepository;

    @InjectMocks
    private OrdenReabastecimientoService ordenService;

    @Test
    void testCrearOrden() {
        // Given
        OrdenReabastecimiento nueva = new OrdenReabastecimiento(null, null, "sucursal-01", null, null, null);
        OrdenReabastecimiento guardada = new OrdenReabastecimiento(1L, null, "sucursal-01", null, EstadoOrden.PENDIENTE, null);
        when(ordenRepository.save(any(OrdenReabastecimiento.class))).thenReturn(guardada);

        // When
        OrdenReabastecimiento resultado = ordenService.crearOrden(nueva);

        // Then
        assertNotNull(resultado);
        assertEquals(EstadoOrden.PENDIENTE, resultado.getEstado());
        verify(ordenRepository, times(1)).save(any(OrdenReabastecimiento.class));
    }

    @Test
    void testAprobarOrden() {
        // Given
        OrdenReabastecimiento orden = new OrdenReabastecimiento(1L, null, "sucursal-01", null, EstadoOrden.PENDIENTE, null);
        when(ordenRepository.findById(1L)).thenReturn(Optional.of(orden));
        when(ordenRepository.save(orden)).thenReturn(orden);

        // When
        ordenService.aprobarOrden(1L);

        // Then
        assertEquals(EstadoOrden.APROBADA, orden.getEstado());
        verify(ordenRepository, times(1)).save(orden);
    }

    @Test
    void testAprobarOrdenNoExistente() {
        // Given
        when(ordenRepository.findById(99L)).thenReturn(Optional.empty());

        // When
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> ordenService.aprobarOrden(99L));

        // Then
        assertEquals("No existe la orden de reabastecimiento", exception.getMessage());
        verify(ordenRepository, never()).save(any(OrdenReabastecimiento.class));
    }

    @Test
    void testRegistrarRecepcion() {
        // Given
        OrdenReabastecimiento orden = new OrdenReabastecimiento(1L, null, "sucursal-01", null, EstadoOrden.APROBADA, null);
        when(ordenRepository.findById(1L)).thenReturn(Optional.of(orden));
        when(ordenRepository.save(orden)).thenReturn(orden);

        // When
        ordenService.registrarRecepcion(1L);

        // Then
        assertEquals(EstadoOrden.RECIBIDA, orden.getEstado());
        verify(ordenRepository, times(1)).save(orden);
    }

    @Test
    void testRegistrarRecepcionNoExistente() {
        // Given
        when(ordenRepository.findById(99L)).thenReturn(Optional.empty());

        // When
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> ordenService.registrarRecepcion(99L));

        // Then
        assertEquals("No existe la orden de reabastecimiento", exception.getMessage());
    }
}
