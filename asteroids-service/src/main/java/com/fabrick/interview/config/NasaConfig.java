package com.fabrick.interview.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class NasaConfig {

    @Value("${external.nasa.base-url}")
    private String nasaBaseUrl;

    @Bean
    public WebClient nasaWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(nasaBaseUrl)
                .build();
    }
}
