package com.redmopag.documentmanagment.documentservice.client;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Component
public class FileStorageClient {
    private static final String STORAGE_FILE_URI_PREDICATE = "/storage/file/upload";

    private final WebClient webClient;

    public FileStorageClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Void> sendFile(Long fileId, MultipartFile file) throws IOException {
        ByteArrayResource fileAsResource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };

        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add("file-id", fileId.toString());
        formData.add("file", fileAsResource);

        return webClient.post()
                .uri(STORAGE_FILE_URI_PREDICATE)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(formData))
                .retrieve()
                .toBodilessEntity()
                .then();

    }

    public String generatePresignedUrl(String fileKey, int expirationMinutes) {
        return "";
    }
}
