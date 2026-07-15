# ms-logistica

**Perfulandia SPA — Microservicio de Logística**
DSY1103 Desarrollo FullStack 1 · Evaluación Final Transversal · 2026

---

## Descripción del dominio

Gestiona la cadena logística completa de Perfulandia SPA: creación y seguimiento de envíos con máquina de estados, optimización de rutas de entrega entre sucursales, registro y gestión de proveedores, y flujo de aprobación de órdenes de reabastecimiento (PENDIENTE → APROBADA → RECIBIDA).

---

## Tecnologías

- Java 25 · Spring Boot 4.0.7 · Maven · Packaging WAR
- Spring Data JPA + Hibernate
- Bean Validation JSR 380 · SLF4J
- springdoc-openapi 2.8.9 · JUnit 5 + Mockito · H2

---

## Puerto y base de datos

| Propiedad | Valor |
|---|---|
| Puerto | `8087` |
| Base de datos | `logistica_db` |
| Swagger UI | `http://localhost:8087/doc/swagger-ui.html` |

---

## Diagrama de entidades

```
┌──────────────────────────────┐    ┌──────────────────────────────────┐
│            Envio              │    │          OrdenReabastecimiento    │
├──────────────────────────────┤    ├──────────────────────────────────┤
│ id: Long (PK)                │    │ id: Long (PK)                    │
│ idVenta: String              │    │ idProveedor: Long (FK)           │
│ idSucursalOrigen: String     │    │ idSucursal: String               │
│ direccionDestino: String     │    │ productos: List<String>          │
│ estado: EstadoEnvio          │    │ estado: EstadoOrden              │
│ fechaEstimada: DateTime      │    │ fechaOrden: DateTime             │
│ idRuta: String               │    └──────────────────────────────────┘
└──────────────────────────────┘              │ ManyToOne
                                              ▼
┌──────────────────────────────┐    ┌──────────────────────────────────┐
│         RutaEntrega          │    │           Proveedor              │
├──────────────────────────────┤    ├──────────────────────────────────┤
│ id: Long (PK)                │    │ id: Long (PK)                    │
│ origen: String               │    │ razonSocial: String @NotBlank    │
│ destino: String              │    │ rutNif: String @NotBlank         │
└──────────────────────────────┘    │ contacto: String                 │
                                    │ estado: EstadoProveedor          │
EstadoEnvio:                        │ condicionesPago: String          │
  PREPARANDO | EN_TRANSITO          └──────────────────────────────────┘
  ENTREGADO | DEVUELTO
EstadoOrden: PENDIENTE | APROBADA | RECIBIDA
EstadoProveedor: ACTIVO | INACTIVO
```

---

## Estructura del proyecto (patrón CSR)

```
ms-logistica/
└── src/main/java/com/perfulandia/ms_logistica/
    ├── controller/   EnvioController, ProveedorController,
    │                 OrdenReabastecimientoController, RutaEntregaController
    ├── service/      EnvioService, ProveedorService,
    │                 OrdenReabastecimientoService, RutaEntregaService
    ├── repository/   EnvioRepository, ProveedorRepository,
    │                 OrdenReabastecimientoRepository, RutaEntregaRepository
    ├── model/        Envio, Proveedor, OrdenReabastecimiento, RutaEntrega
    │                 EstadoEnvio, EstadoOrden, EstadoProveedor
    ├── dto/          RutaRequestDTO
    └── exception/    GlobalExceptionHandler
```

---

## Endpoints REST

| Método | Ruta | Descripción | Código |
|--------|------|-------------|--------|
| `POST` | `/api/v1/envios` | Crear envío | 200 |
| `GET` | `/api/v1/envios/{id}` | Seguir envío | 200 |
| `PUT` | `/api/v1/envios/{id}` | Actualizar envío | 200 |
| `PUT` | `/api/v1/envios/{id}/estado` | Cambiar estado | 204 |
| `POST` | `/api/v1/rutas/optimizar` | Optimizar ruta | 200 |
| `POST` | `/api/v1/proveedores` | Registrar proveedor | 200 |
| `PUT` | `/api/v1/proveedores/{id}` | Actualizar proveedor | 200 |
| `POST` | `/api/v1/ordenes-reabastecimiento` | Crear orden | 200 |
| `PUT` | `/api/v1/ordenes-reabastecimiento/{id}/aprobar` | Aprobar orden | 204 |
| `PUT` | `/api/v1/ordenes-reabastecimiento/{id}/recepcion` | Registrar recepción | 204 |

### Ejemplo POST `/api/v1/envios`

Request:
```json
{
  "idVenta": "venta-001",
  "idSucursalOrigen": "1",
  "direccionDestino": "Av. Libertad 456, Concepción"
}
```

Response 200:
```json
{
  "id": 1,
  "idVenta": "venta-001",
  "idSucursalOrigen": "1",
  "direccionDestino": "Av. Libertad 456, Concepción",
  "estado": "PREPARANDO"
}
```

### Ejemplo POST `/api/v1/rutas/optimizar`

Request:
```json
{
  "origen": "Bodega Central Santiago",
  "destinos": ["Concepción", "Viña del Mar"]
}
```

Response 400:
```
Debe indicar al menos un destino
```

---

## Reglas de negocio

| Método | Regla |
|--------|-------|
| `crearEnvio()` | Fuerza el estado inicial `PREPARANDO` — el cliente no puede crear un envío en otro estado |
| `actualizarEnvio()` | Valida que el envío exista antes de modificar |
| `seguirEnvio()` | Lanza RuntimeException si el envío no existe → HTTP 400 |
| `actualizarEstadoEnvio()` | Valida existencia del envío antes de cambiar estado |
| `optimizarRuta()` | La lista de destinos no puede estar vacía → RuntimeException → HTTP 400 |
| `registrarProveedor()` | Asigna automáticamente estado ACTIVO al nuevo proveedor |
| `actualizarProveedor()` | Valida que el proveedor exista antes de modificar |
| `crearOrden()` | Fuerza estado inicial PENDIENTE y registra fecha automáticamente |
| `aprobarOrden()` | PENDIENTE → APROBADA; valida existencia previa |
| `registrarRecepcion()` | APROBADA → RECIBIDA; valida existencia previa |

---

## Validaciones (Bean Validation JSR 380)

| Campo | Anotación | Mensaje |
|-------|-----------|---------|
| `Proveedor.razonSocial` | `@NotBlank` | "La razón social es obligatoria" |
| `Proveedor.rutNif` | `@NotBlank` | "El RUT/NIF es obligatorio" |

---

## Manejo de errores

| Excepción | HTTP | Respuesta |
|-----------|------|-----------|
| `RuntimeException` | 400 | Mensaje en texto plano |
| `MethodArgumentNotValidException` | 400 | JSON con campos inválidos |

---

## Pruebas unitarias

```bash
./mvnw clean test
```

| Clase | Casos cubiertos |
|-------|-----------------|
| `EnvioServiceTest` | Crear OK, seguir OK, seguir error, actualizar OK, actualizar error, cambiar estado OK |
| `ProveedorServiceTest` | Registrar OK, actualizar OK, actualizar error |
| `OrdenReabastecimientoServiceTest` | Crear OK, aprobar OK, aprobar error, recepción OK |
| `RutaEntregaServiceTest` | Optimizar OK, sin destinos error |
| `EnvioControllerTest` | POST 200, GET 200, GET 400, PUT estado 204, PUT estado 400 |
| `ProveedorControllerTest` | POST 200, PUT 200, PUT 400 |
| `OrdenReabastecimientoControllerTest` | POST 200, aprobar 204, aprobar 400, recepción 204 |
| `RutaEntregaControllerTest` | POST 200, POST sin destinos 400 |

---

## Configuración

### `src/main/resources/application.properties`
```properties
spring.application.name=ms-logistica
server.port=8087

spring.datasource.url=jdbc:mysql://localhost:3306/logistica_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.path=/doc/swagger-ui.html
```

---

## Ejecución local

```sql
CREATE DATABASE logistica_db;
```

```bash
cd ms-logistica
./mvnw spring-boot:run
./mvnw clean test
```
