package com.redmopag.documentmanagment.documentservice.client;

import com.redmopag.documentmanagment.documentservice.exception.FileStorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class DocumentStorageClient extends AbstractRestClient{
    private static final String STORE_DOCUMENT_PREDICATE = "/store";
    private static final FileStorageException STORE_DOCUMENT_EXCEPTION =
            new FileStorageException("Failed to save document to the storage");

    public DocumentStorageClient(RestClient.Builder restClientBuilder,
                                 @Value("${file.storage.service.url}") String fileStorageUrl) {
        super(restClientBuilder.baseUrl(fileStorageUrl).build());
    }

    public String storeDocument(byte[] bodyValue) {
        ResponseEntity<String> storagePath =
                sendPostRequest(bodyValue, STORE_DOCUMENT_PREDICATE, String.class);
        return checkSuccessfulAndReturnResponseBody(storagePath, STORE_DOCUMENT_EXCEPTION);
    }
}
