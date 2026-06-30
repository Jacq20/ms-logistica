package com.perfulandia.ms_logistica.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "envios")
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_venta", nullable = false)
    private String idVenta;

    @Column(name = "id_sucursal_origen", nullable = false)
    private String idSucursalOrigen;

    @Column(name = "direccion_destino", nullable = false)
    private String direccionDestino;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEnvio estado;

    @Column(name = "fecha_estimada")
    private LocalDateTime fechaEstimada;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ruta_id")
    private RutaEntrega ruta;
}
