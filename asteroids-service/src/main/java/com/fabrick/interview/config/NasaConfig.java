package com.fabrick.interview.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration class responsible for defining the networking components required to communicate
 * with the NASA NeoWs (Near Earth Object Web Service) API.
 * <p>
 * This class declares the {@link WebClient} bean, pre-configured with the specific Base URL
 * for NASA services. This ensures that the base path is consistent across the application
 * and easily configurable via properties.
 * </p>
 */
@Configuration
public class NasaConfig {

    /**
     * The base URL for the NASA API, injected from the application properties
     * (e.g., {@code https://api.nasa.gov/neo/rest/v1/neo}).
     */
    @Value("${external.nasa.base-url}")
    private String nasaBaseUrl;

    /**
     * Creates and configures a specific {@link WebClient} instance for NASA API interactions.
     * <p>
     * By configuring the {@code baseUrl} at this stage, the service layer ({@code NasaApiClient})
     * does not need to worry about the absolute path, keeping the code cleaner and more maintainable.
     * Using {@link WebClient} ensures the application remains non-blocking and reactive.
     * </p>
     *
     * @param builder The Spring Boot auto-configured {@link WebClient.Builder}.
     * @return A fully configured {@link WebClient} ready to make requests to NASA endpoints.
     */
    @Bean
    public WebClient nasaWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(nasaBaseUrl)
                .build();
    }
}
