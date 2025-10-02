package sura.pruebalegoback.web.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import sura.pruebalegoback.domain.weather.gateway.WeatherGateway;
import sura.pruebalegoback.domain.weather.model.WeatherResponse;

@Component
public class WeatherGatewayImpl implements WeatherGateway {

    private final WebClient webClient;
    private final String weatherUrl;

    public WeatherGatewayImpl(WebClient.Builder webClientBuilder,
                              @Value("${weather.api.url}") String weatherUrl) {
        this.webClient = webClientBuilder.build();
        this.weatherUrl = weatherUrl;
    }

    @Override
    public Mono<WeatherResponse> getWeather() {
        return webClient.get()
                .uri(weatherUrl)
                .retrieve()
                .bodyToMono(WeatherResponse.class)
                .timeout(java.time.Duration.ofSeconds(5))
                .retry(2)
                .onErrorResume(e -> Mono.error(new RuntimeException("Error al consumir el API de clima", e)));
    }
}