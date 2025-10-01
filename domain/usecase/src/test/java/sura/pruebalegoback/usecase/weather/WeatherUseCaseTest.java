package sura.pruebalegoback.usecase.weather;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import sura.pruebalegoback.domain.weather.gateway.WeatherGateway;
import sura.pruebalegoback.domain.weather.model.WeatherResponse;

import java.util.HashMap;

import static org.mockito.Mockito.*;

class WeatherUseCaseTest {

    @Mock
    private WeatherGateway weatherGateway;

    @InjectMocks
    private WeatherUseCase weatherUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getWeather_success() {
        WeatherResponse response = new WeatherResponse();
        response.setCurrent(new HashMap<>());
        when(weatherGateway.getWeather()).thenReturn(Mono.just(response));

        StepVerifier.create(weatherUseCase.getWeather())
                .expectNextMatches(r -> r.getCurrent() != null)
                .verifyComplete();
        verify(weatherGateway).getWeather();
    }

    @Test
    void getWeather_error() {
        when(weatherGateway.getWeather()).thenReturn(Mono.error(new RuntimeException("API error")));

        StepVerifier.create(weatherUseCase.getWeather())
                .expectErrorMatches(e -> e instanceof RuntimeException && e.getMessage().equals("API error"))
                .verify();
        verify(weatherGateway).getWeather();
    }
}