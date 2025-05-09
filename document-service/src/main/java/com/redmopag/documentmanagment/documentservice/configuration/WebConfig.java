package com.redmopag.documentmanagment.documentservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebConfig {
    @Value("${file.storage.service.url}")
    private String storageFileBaseUrl;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(storageFileBaseUrl)
                .build();
    }
}
