package sura.pruebalegoback.domain.weather.gateway;

import reactor.core.publisher.Mono;
import sura.pruebalegoback.domain.weather.model.WeatherResponse;

public interface WeatherGateway {
    Mono<WeatherResponse> getWeather();
}