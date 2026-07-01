package com.perfulandia.ms_logistica.dto;

import java.util.List;

public record RutaRequestDTO(
    String origen,
    List<String> destinos
) {}
