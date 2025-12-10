package com.fabrick.interview.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI fabrickOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Fabrick Interview - Asteroids Service")
                        .description("Reactive REST API to analyze asteroid paths across the Solar System using NASA NeoWs API.")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Fabrick Developer")
                                .email("fabrick-developer@example.com")));
    }
}