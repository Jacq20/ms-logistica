package com.perfulandia.ms_logistica.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfulandia.ms_logistica.model.Envio;
import com.perfulandia.ms_logistica.model.EstadoEnvio;
import com.perfulandia.ms_logistica.service.EnvioService;

@WebMvcTest(EnvioController.class)
@ActiveProfiles("test")
public class EnvioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockitoBean
    private EnvioService envioService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testCrearEnvio() throws Exception {
        Envio nuevo = new Envio(null, "venta-1", "suc-1", "Calle 123", null, null, null);
        Envio guardado = new Envio(1L, "venta-1", "suc-1", "Calle 123", EstadoEnvio.PREPARANDO, null, null);
        Mockito.when(envioService.crearEnvio(any(Envio.class))).thenReturn(guardado);

        mockMvc.perform(post("/api/v1/envios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.estado").value("PREPARANDO"));
    }

    @Test
    void testSeguirEnvio() throws Exception {
        Envio envio = new Envio(1L, "venta-1", "suc-1", "Calle 123", EstadoEnvio.EN_TRANSITO, null, null);
        Mockito.when(envioService.seguirEnvio(1L)).thenReturn(envio);

        mockMvc.perform(get("/api/v1/envios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("EN_TRANSITO"));
    }

    @Test
    void testSeguirEnvioInexistente() throws Exception {
        Mockito.when(envioService.seguirEnvio(99L))
                .thenThrow(new RuntimeException("No existe el envio"));

        mockMvc.perform(get("/api/v1/envios/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testActualizarEstadoEnvio() throws Exception {
        Mockito.doNothing().when(envioService).actualizarEstadoEnvio(eq(1L), eq(EstadoEnvio.ENTREGADO));

        mockMvc.perform(put("/api/v1/envios/1/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"ENTREGADO\""))
                .andExpect(status().isNoContent());
    }
}
