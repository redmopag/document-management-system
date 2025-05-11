package com.redmopag.documentmanagment.documentservice.client;

import com.redmopag.documentmanagment.common.TextResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class TextClient {
    private static final String SEARCH_TEXT_URL = "/text/search";
    private static final String GET_TEXT_BY_ID_URL = "/text";

    private final RestClient restClient;

    public TextClient(
            @Value(("${text.service.url}")) String textServiceUrl,
            RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl(textServiceUrl).build();
    }

    public List<TextResponse> search(String text) {
        return restClient.get()
                .uri(builder -> builder
                        .path(SEARCH_TEXT_URL)
                        .queryParam("text", text)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public TextResponse getDocumentTextById(String id) {
        return restClient.get()
                .uri(builder -> builder
                        .path(GET_TEXT_BY_ID_URL)
                        .queryParam("id", id)
                        .build())
                .retrieve()
                .body(TextResponse.class);
    }
}
