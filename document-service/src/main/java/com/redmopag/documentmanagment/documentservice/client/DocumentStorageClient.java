package com.redmopag.documentmanagment.documentservice.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.net.URI;
import java.util.UUID;

@Component
public class DocumentStorageClient {
    private final S3Client s3Client;
    @Value("${file.storage.bucket}")
    private String bucket;
    @Value("${file.storage.url}")
    private String minIoUrl;

    public DocumentStorageClient() {
        this.s3Client = S3Client.builder()
                .endpointOverride(URI.create(minIoUrl))
                .region(Region.EU_WEST_3)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create("minioAdmin", "minioAdmin")))
                .forcePathStyle(true)
                .build();
    }

    public String upload(byte[] content, String fileName) {
        String documentKey = UUID.randomUUID() + "-" + fileName;
        PutObjectRequest putObjectRequest = buildPutRequest(documentKey);
        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(content));
        return documentKey;
    }

    private PutObjectRequest buildPutRequest(String documentKey) {
        return PutObjectRequest.builder()
                .bucket(bucket)
                .key(documentKey)
                .contentType("application/octet-stream")
                .build();
    }
}
