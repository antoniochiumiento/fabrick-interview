package com.fabrick.interview.client;

import com.fabrick.interview.exception.AsteroidNotFoundException;
import com.fabrick.interview.exception.NasaServiceException;
import com.fabrick.interview.model.nasa.NasaNeoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Client service responsible for interacting with the NASA NeoWs (Near Earth Object Web Service) API.
 * <p>
 * This component handles the HTTP communication using a non-blocking {@link WebClient} and implements
 * a caching mechanism to optimize performance and reduce external API usage.
 * </p>
 */
@Service
public class NasaApiClient {

    private static final Logger logger = LoggerFactory.getLogger(NasaApiClient.class);

    private final WebClient webClient;
    private final String apiKey;

    /**
     * Constructs a new NasaApiClient.
     *
     * @param webClient The pre-configured WebClient instance (usually with base URL set).
     * @param apiKey    The NASA API Key injected from the application properties.
     */
    public NasaApiClient(WebClient webClient, @Value("${external.nasa.api-key}") String apiKey) {
        this.webClient = webClient;
        this.apiKey = apiKey;
    }

    /**
     * Retrieves detailed information about a specific asteroid by its ID.
     * <p>
     * This method utilizes Spring's caching abstraction. If the data for the given {@code asteroidId}
     * is already present in the "asteroids" cache, it is returned immediately. Otherwise, a non-blocking
     * HTTP GET request is made to the NASA API.
     * </p>
     *
     * @param asteroidId The unique SPK-ID of the asteroid (e.g., "3542519").
     * @return A {@link Mono} emitting the {@link NasaNeoResponse} containing the asteroid's data,
     * or an empty/error signal if the retrieval fails.
     */
    @Cacheable("asteroids")
    public Mono<NasaNeoResponse> getAsteroidData(String asteroidId) {
        return Mono.defer(() -> {
            logger.info("Cache MISS - Calling NASA External API for AsteroidID: {}", asteroidId);
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/{asteroidId}")
                            .queryParam("api_key", apiKey)
                            .build(asteroidId))
                    .retrieve()
                    .onStatus(
                            status -> status.value() == 404,
                            response -> Mono.error(new AsteroidNotFoundException(asteroidId))
                    )
                    .onStatus(
                            status -> status.is5xxServerError(),
                            response -> Mono.error(new NasaServiceException("NASA API is currently unavailable."))
                    )
                    .bodyToMono(NasaNeoResponse.class);
        }).cache();
    }
}
