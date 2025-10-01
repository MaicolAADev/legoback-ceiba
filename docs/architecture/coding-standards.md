# Estándares de Código - Proyecto Gestión de Pacientes

## 1. Estructura y Organización
- Seguir la estructura de paquetes por dominio (`model`, `usecase`, `infraestructure`, `entry-points`).
- Usar nombres descriptivos y en inglés para clases, métodos y variables.
- Separar claramente las capas: controlador, servicio, repositorio, modelo.

## 2. Java y Spring WebFlux
- Usar siempre programación reactiva (Mono, Flux) en controladores y servicios.
- Prohibido el uso de `.block()` o `.subscribe()` fuera de pruebas.
- Usar operadores Reactor: `map`, `flatMap`, `filter`, `switchIfEmpty`, `just`, `create`, `defer`, `zip` según corresponda.
- Para operaciones bloqueantes (ej. Apache POI), usar `subscribeOn(Schedulers.boundedElastic())`.
- Inyectar dependencias con constructor, evitar `@Autowired` en campos.

## 3. Persistencia R2DBC
- Usar repositorios reactivos (`ReactiveCrudRepository` o similar).
- Todas las consultas deben retornar `Mono` o `Flux`.
- Manejar errores de conexión y validación con `onErrorResume` y `onErrorReturn`.

## 4. Manejo de Errores
- Usar `onErrorResume` para errores técnicos (DB, red, etc.).
- Usar `onErrorReturn` para errores de negocio (validaciones).
- Implementar un `@ControllerAdvice` para mapear excepciones a respuestas HTTP.
- Diferenciar claramente errores técnicos y de negocio en logs y respuestas.

## 5. Logs
- Usar Logback y SLF4J para logging.
- Niveles: `error`, `warn`, `info`, `debug`.
- No agregar logs innecesarios; ajustar niveles de log en configuración.
- Loggear errores y eventos críticos, nunca información sensible.

## 6. Consumo de APIs Externas
- Usar `WebClient` de manera reactiva.
- Mapear respuestas con Jackson (`ObjectMapper`).
- Manejar errores de red y parsing con `onErrorResume`.
- No usar `.block()` para obtener respuestas.

## 7. Mensajería (RabbitMQ)
- Usar librerías compatibles con WebFlux (ej. `reactor-rabbitmq`).
- Publicar eventos de manera reactiva.
- Consumidores deben manejar ack manual y reintentos.

## 8. Exportación a Excel
- Usar Apache POI en hilos bloqueantes con `subscribeOn`.
- No bloquear el hilo principal.

## 9. Pruebas
- Pruebas unitarias obligatorias para lógica de negocio y controladores.
- Pruebas de integración para flujos completos (DB, MQ, WebClient).
- Cobertura mínima: 80% líneas y ramas.
- Usar JUnit 5, Mockito, Testcontainers, y JaCoCo para coverage.

## 10. Otros
- Usar comentarios solo cuando el código no sea autoexplicativo.
- Mantener documentación técnica y de negocio actualizada.
- Seguir convenciones de formato (Google Java Style o similar).
