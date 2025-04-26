package com.redmopag.documentmanagment.documentservice.client;

import com.redmopag.documentmanagment.documentservice.dto.OcrResponse;
import com.redmopag.documentmanagment.documentservice.exception.OcrException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class OcrClient extends AbstractRestClient {
    private static final String RECOGNIZE_URI_PREDICATE = "/recognize";
    private static final OcrException RECOGNIZE_EXCEPTION =
            new OcrException("Failed to recognize text in file");

    public OcrClient(RestClient.Builder restClientBuilder,
                     @Value("${ocr.service.url}") String ocrServiceUrl) {
        super(restClientBuilder
                .baseUrl(ocrServiceUrl)
                .build());
    }

    public OcrResponse recognizeText(byte[] bodyValue) {
        ResponseEntity<OcrResponse> responseEntity =
                sendPostRequest(bodyValue, RECOGNIZE_URI_PREDICATE, OcrResponse.class);
        return checkSuccessfulAndReturnResponseBody(responseEntity, RECOGNIZE_EXCEPTION);
    }
}
