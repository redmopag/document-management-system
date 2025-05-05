package com.redmopag.documentmanagment.documentservice.client.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DocumentStorageClient {
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final S3Director s3Director;

    public String upload(byte[] content, String fileName) {
        String documentKey = UUID.randomUUID() + "-" + fileName;
        PutObjectRequest putObjectRequest = s3Director.buildPutRequest(documentKey);
        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(content));
        return documentKey;
    }

    public String generatePresignedUrl(String documentKey, int expirationMinutes) {
        GetObjectRequest getObjectRequest = s3Director.buildGetRequest(documentKey);
        GetObjectPresignRequest presignRequest =
                s3Director.buildPresignReqeust(expirationMinutes, getObjectRequest);
        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }
}
