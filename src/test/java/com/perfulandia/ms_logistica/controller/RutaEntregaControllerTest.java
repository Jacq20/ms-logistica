package com.perfulandia.ms_logistica.controller;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfulandia.ms_logistica.model.RutaEntrega;
import com.perfulandia.ms_logistica.service.RutaEntregaService;

@WebMvcTest(RutaEntregaController.class)
@ActiveProfiles("test")
public class RutaEntregaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockitoBean
    private RutaEntregaService rutaEntregaService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testOptimizarRuta() throws Exception {
        List<String> destinos = Arrays.asList("Concepcion", "Talcahuano");
        RutaEntrega ruta = new RutaEntrega(1L, "Bodega Central", "Concepcion");
        Mockito.when(rutaEntregaService.optimizarRuta("Bodega Central", destinos)).thenReturn(ruta);

        mockMvc.perform(post("/api/v1/rutas/optimizar")
                        .param("origen", "Bodega Central")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(destinos)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.destino").value("Concepcion"));
    }

    @Test
    void testOptimizarRutaSinDestinos() throws Exception {
        Mockito.when(rutaEntregaService.optimizarRuta("Bodega Central", List.of()))
                .thenThrow(new RuntimeException("Debe indicar al menos un destino"));

        mockMvc.perform(post("/api/v1/rutas/optimizar")
                        .param("origen", "Bodega Central")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[]"))
                .andExpect(status().isNotFound());
    }
}
