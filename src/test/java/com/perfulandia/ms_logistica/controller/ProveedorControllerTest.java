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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfulandia.ms_logistica.model.EstadoProveedor;
import com.perfulandia.ms_logistica.model.Proveedor;
import com.perfulandia.ms_logistica.service.ProveedorService;

@WebMvcTest(ProveedorController.class)
@ActiveProfiles("test")
public class ProveedorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockitoBean
    private ProveedorService proveedorService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testRegistrarProveedor() throws Exception {
        Proveedor nuevo = new Proveedor(null, "Distribuidora ABC", "76.123.456-7", "contacto@abc.cl", null, "30 dias");
        Proveedor guardado = new Proveedor(1L, "Distribuidora ABC", "76.123.456-7", "contacto@abc.cl", EstadoProveedor.ACTIVO, "30 dias");
        Mockito.when(proveedorService.registrarProveedor(any(Proveedor.class))).thenReturn(guardado);

        mockMvc.perform(post("/api/v1/proveedores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.estado").value("ACTIVO"));
    }

    @Test
    void testActualizarProveedor() throws Exception {
        Proveedor actualizado = new Proveedor(1L, "Distribuidora ABC Renovada", "76.123.456-7", "nuevo@abc.cl", EstadoProveedor.ACTIVO, "60 dias");
        Mockito.when(proveedorService.actualizarProveedor(eq(1L), any(Proveedor.class))).thenReturn(actualizado);

        mockMvc.perform(put("/api/v1/proveedores/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.razonSocial").value("Distribuidora ABC Renovada"));
    }

    @Test
    void testActualizarProveedorInexistente() throws Exception {
        Proveedor datos = new Proveedor(null, "Ghost", "0-0", null, null, null);
        Mockito.when(proveedorService.actualizarProveedor(eq(99L), any(Proveedor.class)))
                .thenThrow(new RuntimeException("No existe el proveedor"));

        mockMvc.perform(put("/api/v1/proveedores/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(datos)))
                .andExpect(status().isNotFound());
    }
}
