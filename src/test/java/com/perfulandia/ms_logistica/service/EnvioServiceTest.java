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

import com.perfulandia.ms_logistica.model.Envio;
import com.perfulandia.ms_logistica.model.EstadoEnvio;
import com.perfulandia.ms_logistica.repository.EnvioRepository;

@ExtendWith(MockitoExtension.class)
class EnvioServiceTest {

    @Mock
    private EnvioRepository envioRepository;

    @InjectMocks
    private EnvioService envioService;

    @Test
    void testCrearEnvio() {
        // Given
        Envio nuevo = new Envio(null, "venta-001", "sucursal-01", "Calle Falsa 123", null, null, null);
        Envio guardado = new Envio(1L, "venta-001", "sucursal-01", "Calle Falsa 123", EstadoEnvio.PREPARANDO, null, null);
        when(envioRepository.save(any(Envio.class))).thenReturn(guardado);

        // When
        Envio resultado = envioService.crearEnvio(nuevo);

        // Then
        assertNotNull(resultado);
        assertEquals(EstadoEnvio.PREPARANDO, resultado.getEstado());
        verify(envioRepository, times(1)).save(any(Envio.class));
    }

    @Test
    void testActualizarEnvio() {
        // Given
        Envio existente = new Envio(1L, "venta-001", "sucursal-01", "Calle Falsa 123", EstadoEnvio.PREPARANDO, null, null);
        Envio datosNuevos = new Envio(null, "venta-001", "sucursal-01", "Av. Nueva 456", null, null, null);
        when(envioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(envioRepository.save(existente)).thenReturn(existente);

        // When
        Envio resultado = envioService.actualizarEnvio(1L, datosNuevos);

        // Then
        assertEquals("Av. Nueva 456", resultado.getDireccionDestino());
        verify(envioRepository, times(1)).save(existente);
    }

    @Test
    void testActualizarEnvioNoExistente() {
        // Given
        when(envioRepository.findById(99L)).thenReturn(Optional.empty());

        // When
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> envioService.actualizarEnvio(99L, new Envio()));

        // Then
        assertEquals("No existe el envio", exception.getMessage());
        verify(envioRepository, never()).save(any(Envio.class));
    }

    @Test
    void testSeguirEnvio() {
        // Given
        Envio envio = new Envio(1L, "venta-001", "sucursal-01", "Calle Falsa 123", EstadoEnvio.EN_TRANSITO, null, null);
        when(envioRepository.findById(1L)).thenReturn(Optional.of(envio));

        // When
        Envio resultado = envioService.seguirEnvio(1L);

        // Then
        assertEquals(EstadoEnvio.EN_TRANSITO, resultado.getEstado());
    }

    @Test
    void testSeguirEnvioNoExistente() {
        // Given
        when(envioRepository.findById(99L)).thenReturn(Optional.empty());

        // When
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> envioService.seguirEnvio(99L));

        // Then
        assertEquals("No existe el envio", exception.getMessage());
    }

    @Test
    void testActualizarEstadoEnvio() {
        // Given
        Envio envio = new Envio(1L, "venta-001", "sucursal-01", "Calle Falsa 123", EstadoEnvio.EN_TRANSITO, null, null);
        when(envioRepository.findById(1L)).thenReturn(Optional.of(envio));
        when(envioRepository.save(envio)).thenReturn(envio);

        // When
        envioService.actualizarEstadoEnvio(1L, EstadoEnvio.ENTREGADO);

        // Then
        assertEquals(EstadoEnvio.ENTREGADO, envio.getEstado());
        verify(envioRepository, times(1)).save(envio);
    }

    @Test
    void testActualizarEstadoEnvioNoExistente() {
        // Given
        when(envioRepository.findById(99L)).thenReturn(Optional.empty());

        // When
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> envioService.actualizarEstadoEnvio(99L, EstadoEnvio.ENTREGADO));

        // Then
        assertEquals("No existe el envio", exception.getMessage());
        verify(envioRepository, never()).save(any(Envio.class));
    }
}
