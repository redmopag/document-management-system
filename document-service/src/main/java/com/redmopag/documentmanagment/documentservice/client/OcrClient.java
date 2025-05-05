package com.redmopag.documentmanagment.documentservice.client;

import com.redmopag.documentmanagment.documentservice.dto.OcrResponse;
import com.redmopag.documentmanagment.documentservice.exception.OcrException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class OcrClient {
    private static final String RECOGNIZE_URI_PREDICATE = "/ocr/recognize";
    protected final RestClient restClient;

    public OcrClient(RestClient.Builder restClientBuilder,
                     @Value("${ocr.service.url}") String ocrServiceUrl) {
        restClient = restClientBuilder.baseUrl(ocrServiceUrl).build();
    }

    public OcrResponse recognizeFile(MultipartFile file) throws IOException {
        ResponseEntity<OcrResponse> responseEntity = sendRecognitionRequest(file);
        if (isResponseSuccessful(responseEntity)) {
            return responseEntity.getBody();
        } else {
            throw new OcrException("Не удалось распознать текст в файле: " +
                    file.getOriginalFilename());
        }
    }

    private ResponseEntity<OcrResponse> sendRecognitionRequest(MultipartFile file)
            throws IOException {
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("description", "Это описание файла");
        bodyBuilder.part("file", new ByteArrayResource(file.getBytes()) {
                    @Override
                    public String getFilename() {
                        return file.getOriginalFilename();
                    }
                })
                .contentType(MediaType.TEXT_PLAIN);
        return restClient
                .post()
                .uri(RECOGNIZE_URI_PREDICATE)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(bodyBuilder.build())
                .retrieve()
                .toEntity(OcrResponse.class);
    }

    private boolean isResponseSuccessful(ResponseEntity<OcrResponse> responseEntity) {
        return responseEntity.getStatusCode().is2xxSuccessful();
    }
}
