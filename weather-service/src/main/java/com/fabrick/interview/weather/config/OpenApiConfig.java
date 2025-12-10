package com.fabrick.interview.weather.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI weatherOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Fabrick Interview - Weather Service")
                        .description("Reactive REST API to find weather stations and airports using Aviation Weather Center API.")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Fabrick Developer")
                                .email("fabrick-developer@example.com")));
    }
}