package com.perfulandia.ms_logistica.service;

import java.util.Arrays;
import java.util.Collections;

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

import com.perfulandia.ms_logistica.model.RutaEntrega;
import com.perfulandia.ms_logistica.repository.RutaEntregaRepository;

@ExtendWith(MockitoExtension.class)
class RutaEntregaServiceTest {

    @Mock
    private RutaEntregaRepository rutaEntregaRepository;

    @InjectMocks
    private RutaEntregaService rutaEntregaService;

    @Test
    void testOptimizarRuta() {
        // Given
        RutaEntrega guardada = new RutaEntrega(1L, "Bodega Central", "Sucursal Vina del Mar");
        when(rutaEntregaRepository.save(any(RutaEntrega.class))).thenReturn(guardada);

        // When
        RutaEntrega resultado = rutaEntregaService.optimizarRuta("Bodega Central",
                Arrays.asList("Sucursal Vina del Mar", "Sucursal Concepcion"));

        // Then
        assertNotNull(resultado);
        assertEquals("Bodega Central", resultado.getOrigen());
        verify(rutaEntregaRepository, times(1)).save(any(RutaEntrega.class));
    }

    @Test
    void testOptimizarRutaSinDestinos() {
        // When
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> rutaEntregaService.optimizarRuta("Bodega Central", Collections.emptyList()));

        // Then
        assertEquals("Debe indicar al menos un destino", exception.getMessage());
        verify(rutaEntregaRepository, never()).save(any(RutaEntrega.class));
    }
}
