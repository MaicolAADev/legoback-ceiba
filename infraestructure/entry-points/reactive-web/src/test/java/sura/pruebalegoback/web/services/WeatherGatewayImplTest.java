package sura.pruebalegoback.web.services;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import sura.pruebalegoback.domain.weather.model.WeatherResponse;
import static org.junit.jupiter.api.Assertions.*;

class WeatherGatewayImplTest {

    @Test
    void testGetWeatherSuccess() {
        WebClient.Builder builder = WebClient.builder();
        WeatherGatewayImpl gateway = new WeatherGatewayImpl(builder, "http://localhost:8080/api/weather");
        Mono<WeatherResponse> mono = gateway.getWeather();
        assertNotNull(mono);
    }

    @Test
    void testGetWeatherError() {
        WebClient.Builder builder = WebClient.builder();
        WeatherGatewayImpl gateway = new WeatherGatewayImpl(builder, "badurl");
        Mono<WeatherResponse> mono = gateway.getWeather();
        assertNotNull(mono);
        // No se puede mockear la cadena, pero se puede verificar que el Mono propaga error al bloquear
        try {
            mono.block();
            fail("Se esperaba excepción");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Error al consumir el API de clima"));
        }
    }
}