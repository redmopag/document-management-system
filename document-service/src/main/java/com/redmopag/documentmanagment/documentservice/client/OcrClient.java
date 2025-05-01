package com.redmopag.documentmanagment.documentservice.client;

import com.redmopag.documentmanagment.documentservice.dto.OcrResponse;
import com.redmopag.documentmanagment.documentservice.exception.OcrException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.BodyInserters;

@Component
public class OcrClient {
    private static final String RECOGNIZE_URI_PREDICATE = "/ocr/recognize";
    protected final RestClient restClient;

    public OcrClient(RestClient.Builder restClientBuilder,
                     @Value("${ocr.service.url}") String ocrServiceUrl) {
        restClient = restClientBuilder.baseUrl(ocrServiceUrl).build();
    }

    public OcrResponse recognizeText(byte[] fileContent, String filename) {
        ResponseEntity<OcrResponse> responseEntity = sendRecognitionRequest(fileContent, filename);
        if (isResponseSuccessful(responseEntity)) {
            return responseEntity.getBody();
        } else {
            throw new OcrException("Failed to recognize text in file");
        }
    }

    protected ResponseEntity<OcrResponse> sendRecognitionRequest(byte[] fileContent, String filename) {
        return restClient
                .post()
                .uri(RECOGNIZE_URI_PREDICATE)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(generateMultipart(fileContent, filename)))
                .retrieve()
                .toEntity(OcrResponse.class);
    }

    private MultiValueMap<String, HttpEntity<?>> generateMultipart(byte[] fileContent, String filename) {
        ByteArrayResource resource = new ByteArrayResource(fileContent) {
            @Override
            public String getFilename() {
                return filename;
            }
        };

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        HttpEntity<ByteArrayResource> filePart = new HttpEntity<>(resource, headers);

        MultiValueMap<String, HttpEntity<?>> multipart = new LinkedMultiValueMap<>();
        multipart.add("file", filePart);
        return multipart;
    }

    private boolean isResponseSuccessful(ResponseEntity<OcrResponse> responseEntity) {
        return responseEntity.getStatusCode().is2xxSuccessful();
    }
}
