package sura.pruebalegoback.web.weather;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import sura.pruebalegoback.domain.weather.model.WeatherResponse;
import sura.pruebalegoback.usecase.weather.WeatherUseCase;

@RestController
@RequestMapping("/weather")
public class WeatherQueryService {

    private final WeatherUseCase weatherUseCase;

    public WeatherQueryService(WeatherUseCase weatherUseCase) {
        this.weatherUseCase = weatherUseCase;
    }

    @GetMapping
    public Mono<WeatherResponse> getWeather() {
        return weatherUseCase.getWeather();
    }
}