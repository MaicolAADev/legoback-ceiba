# Flujo de Negocio: Registro de Paciente

## Descripción
Este flujo describe el proceso completo para registrar un paciente en la clínica, incluyendo persistencia, publicación en RabbitMQ y manejo de errores.

## Diagrama de Secuencia
```mermaid
sequenceDiagram
    participant API as API REST
    participant Service as Servicio Paciente
    participant DB as PostgreSQL (R2DBC)
    participant MQ as RabbitMQ
    participant Logger as Logback

    API->>Service: POST /pacientes (datos paciente)
    Service->>DB: Guardar paciente (reactivo)
    DB-->>Service: Paciente guardado
    Service->>MQ: Publicar evento paciente-creado
    MQ-->>Service: Ack publicación
    Service->>API: Respuesta 201 Created
    Service->>Logger: Log info/éxito
    Note over Service,DB: onErrorResume/onErrorReturn para errores técnicos/negocio
    Note over Service: No usar .block() en ningún punto
```

## Manejo de Errores
- onErrorResume para errores técnicos (DB, MQ)
- onErrorReturn para errores de negocio (validaciones)
- ControllerAdvice para respuestas HTTP adecuadas
- Logging de errores y eventos relevantes

## Casos de Prueba
- Registro exitoso
- Error de validación (negocio)
- Error de persistencia (técnico)
- Error al publicar en RabbitMQ

## Métricas
- Tiempo de respuesta
- Tasa de éxito/fallo
- Mensajes publicados en MQ
