# Flujo de Negocio: Consulta de Clima Externo

## Descripción
Este flujo describe cómo el sistema consume una API externa para obtener el estado del clima en Estados Unidos, mapeando la respuesta con Jackson y retornando el resultado vía API.

## Diagrama de Secuencia
```mermaid
sequenceDiagram
    participant API as API REST
    participant Service as Servicio Clima
    participant WebClient as WebClient
    participant ExternalAPI as API Clima Externa
    participant Logger as Logback

    API->>Service: GET /clima?ciudad=XX
    Service->>WebClient: Realizar request a API externa
    WebClient->>ExternalAPI: GET /weather
    ExternalAPI-->>WebClient: Respuesta JSON
    WebClient-->>Service: Datos clima (Jackson)
    Service->>API: Respuesta con datos de clima
    Service->>Logger: Log info/éxito
    Note over Service,WebClient: No usar .block(), mapear con Jackson
    Note over Service: onErrorResume/onErrorReturn para errores
```

## Manejo de Errores
- onErrorResume para errores técnicos (timeout, red, parsing)
- onErrorReturn para errores de negocio (ciudad no encontrada)
- Logging de errores y eventos relevantes

## Casos de Prueba
- Consulta exitosa
- Error de red/API externa
- Error de mapeo Jackson
- Ciudad no encontrada

## Métricas
- Tiempo de respuesta
- Tasa de éxito/fallo
