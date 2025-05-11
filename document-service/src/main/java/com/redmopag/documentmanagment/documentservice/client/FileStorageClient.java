package com.redmopag.documentmanagment.documentservice.client;

import com.redmopag.documentmanagment.common.GenerateLinkResponse;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Component
public class FileStorageClient {
    private static final String STORAGE_FILE_UPLOAD = "/storage/file/upload";
    private static final String STORAGE_FILE_LINK = "/storage/file/link";

    private final WebClient webClient;
    private final RestClient restClient;

    public FileStorageClient(
            @Value("${file.storage.service.url}") String storageFileBaseUrl,
            WebClient.Builder webClientBuilder,
            RestClient.Builder restClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(storageFileBaseUrl).build();
        this.restClient = restClientBuilder.baseUrl(storageFileBaseUrl).build();
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
                .uri(STORAGE_FILE_UPLOAD)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(formData))
                .retrieve()
                .toBodilessEntity()
                .then();

    }

    public GenerateLinkResponse generatePresignedUrl(String objectKey) {
        return restClient.get()
                .uri(builder -> builder
                        .path(STORAGE_FILE_LINK)
                        .queryParam("object-key", objectKey)
                        .build())
                .retrieve()
                .body(GenerateLinkResponse.class);
    }
}
