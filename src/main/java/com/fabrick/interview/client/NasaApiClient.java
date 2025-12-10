package com.fabrick.interview.client;

import com.fabrick.interview.model.nasa.NasaNeoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class NasaApiClient {

    private final WebClient webClient;
    private final String apiKey;

    public NasaApiClient(WebClient webClient, @Value("${external.nasa.api-key}") String apiKey) {
        this.webClient = webClient;
        this.apiKey = apiKey;
    }

    @Cacheable("asteroids")
    public Mono<NasaNeoResponse> getAsteroidData(String asteroidId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/{asteroidId}")
                        .queryParam("api_key", apiKey)
                        .build(asteroidId))
                .retrieve()
                .bodyToMono(NasaNeoResponse.class)
                .cache();
    }
}
