# Flujo de Negocio: Exportación de Pacientes a Excel

## Descripción
Este flujo describe cómo se exportan los pacientes a un archivo Excel usando Apache POI, gestionando hilos bloqueantes de forma reactiva.

## Diagrama de Secuencia
```mermaid
sequenceDiagram
    participant API as API REST
    participant Service as Servicio Paciente
    participant DB as PostgreSQL (R2DBC)
    participant POI as Apache POI
    participant Logger as Logback

    API->>Service: GET /pacientes/exportar
    Service->>DB: Consultar pacientes (Flux)
    DB-->>Service: Lista de pacientes
    Service->>POI: Generar archivo Excel (subscribeOn)
    POI-->>Service: Archivo generado
    Service->>API: Descargar archivo Excel
    Service->>Logger: Log info/éxito
    Note over Service,POI: No usar .block(), usar subscribeOn para hilos bloqueantes
    Note over Service: onErrorResume/onErrorReturn para errores
```

## Manejo de Errores
- onErrorResume para errores técnicos (DB, POI)
- onErrorReturn para errores de negocio
- Logging de errores y eventos relevantes

## Casos de Prueba
- Exportación exitosa
- Error al consultar pacientes
- Error al generar archivo Excel

## Métricas
- Tiempo de generación
- Tamaño del archivo
- Tasa de éxito/fallo
