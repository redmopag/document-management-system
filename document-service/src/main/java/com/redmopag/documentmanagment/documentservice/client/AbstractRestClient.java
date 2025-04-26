package com.redmopag.documentmanagment.documentservice.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

public abstract class AbstractRestClient {
    protected final RestClient restClient;

    protected AbstractRestClient(RestClient restClient) {
        this.restClient = restClient;
    }

    protected<T> ResponseEntity<T> sendPostRequest(byte[] bodyValue,
                                                            String uriPredicate,
                                                            Class<T> responseType) {
        return restClient
                .post()
                .uri(uriPredicate)
                .body(bodyValue)
                .retrieve()
                .toEntity(responseType);
    }

    protected<T> T checkSuccessfulAndReturnResponseBody(
            ResponseEntity<T> responseEntity, RuntimeException exceptionToThrow) {
        if (isResponseSuccessful(responseEntity)) {
            return responseEntity.getBody();
        } else {
            throw exceptionToThrow;
        }
    }

    private boolean isResponseSuccessful(ResponseEntity<?> responseEntity) {
        return responseEntity.getStatusCode().is2xxSuccessful();
    }
}
