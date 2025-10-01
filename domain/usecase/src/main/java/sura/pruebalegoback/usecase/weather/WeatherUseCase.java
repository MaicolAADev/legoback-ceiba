package sura.pruebalegoback.usecase.weather;

import reactor.core.publisher.Mono;
import sura.pruebalegoback.domain.weather.gateway.WeatherGateway;
import sura.pruebalegoback.domain.weather.model.WeatherResponse;

public class WeatherUseCase {
    private final WeatherGateway weatherGateway;

    public WeatherUseCase(WeatherGateway weatherGateway) {
        this.weatherGateway = weatherGateway;
    }

    public Mono<WeatherResponse> getWeather() {
        return weatherGateway.getWeather();
    }
}