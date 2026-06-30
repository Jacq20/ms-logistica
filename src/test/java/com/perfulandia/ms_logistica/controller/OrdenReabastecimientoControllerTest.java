package com.perfulandia.ms_logistica.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfulandia.ms_logistica.model.EstadoOrden;
import com.perfulandia.ms_logistica.model.OrdenReabastecimiento;
import com.perfulandia.ms_logistica.service.OrdenReabastecimientoService;

@WebMvcTest(OrdenReabastecimientoController.class)
@ActiveProfiles("test")
public class OrdenReabastecimientoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockitoBean
    private OrdenReabastecimientoService ordenService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testCrearOrden() throws Exception {
        OrdenReabastecimiento nueva = new OrdenReabastecimiento(null, null, "suc-1", Arrays.asList("Perfume A"), null, null);
        OrdenReabastecimiento guardada = new OrdenReabastecimiento(1L, null, "suc-1", Arrays.asList("Perfume A"), EstadoOrden.PENDIENTE, null);
        Mockito.when(ordenService.crearOrden(any(OrdenReabastecimiento.class))).thenReturn(guardada);

        mockMvc.perform(post("/api/v1/ordenes-reabastecimiento")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nueva)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }

    @Test
    void testAprobarOrden() throws Exception {
        Mockito.doNothing().when(ordenService).aprobarOrden(1L);

        mockMvc.perform(put("/api/v1/ordenes-reabastecimiento/1/aprobar"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testAprobarOrdenInexistente() throws Exception {
        Mockito.doThrow(new RuntimeException("No existe la orden de reabastecimiento"))
                .when(ordenService).aprobarOrden(99L);

        mockMvc.perform(put("/api/v1/ordenes-reabastecimiento/99/aprobar"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testRegistrarRecepcion() throws Exception {
        Mockito.doNothing().when(ordenService).registrarRecepcion(1L);

        mockMvc.perform(put("/api/v1/ordenes-reabastecimiento/1/recepcion"))
                .andExpect(status().isNoContent());
    }
}
