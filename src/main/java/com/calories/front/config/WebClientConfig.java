package com.calories.front.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${backend.url}")
    private String endUrl;

    @Bean
    public WebClient webClient() {
        return WebClient.create(endUrl);
    }
}
