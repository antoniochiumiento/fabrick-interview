package com.fabrick.interview.client;

import com.fabrick.interview.model.nasa.NasaNeoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class NasaApiClient {

    @Value("${external.nasa.api-key}")
    private String apiKey;

    private final WebClient webClient;

    public NasaApiClient(WebClient webClient, String apiKey) {
        this.webClient = webClient;
        this.apiKey = apiKey;
    }

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
