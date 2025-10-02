package sura.pruebalegoback.web.weather;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import sura.pruebalegoback.domain.weather.model.WeatherResponse;
import sura.pruebalegoback.usecase.weather.WeatherUseCase;
import java.util.HashMap;
import java.util.Map;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WebFluxTest(WeatherQueryService.class)
class WeatherIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private WeatherUseCase weatherUseCase;

    @Test
    void testGetWeather() {
        WeatherResponse response = new WeatherResponse();
        Map<String, Object> current = new HashMap<>();
        current.put("temperature", 25.0);
        current.put("description", "Soleado");
        response.setCurrent(current);
        when(weatherUseCase.getWeather()).thenReturn(Mono.just(response));
        webTestClient.get().uri("/weather")
                .exchange()
                .expectStatus().isOk()
                .expectBody(WeatherResponse.class)
                .value(resp -> {
                    assertEquals(current, resp.getCurrent());
                });
    }
}
