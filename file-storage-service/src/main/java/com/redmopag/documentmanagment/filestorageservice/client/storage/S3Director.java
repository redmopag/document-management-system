package com.redmopag.documentmanagment.filestorageservice.client.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.time.Duration;

@Component
public class S3Director {
    @Value("${file.storage.bucket}")
    private String bucket;

    public PutObjectRequest buildPutRequest(String documentKey) {
        return PutObjectRequest.builder()
                .bucket(bucket)
                .key(documentKey)
                .contentType("application/octet-stream")
                .build();
    }

    public GetObjectRequest buildGetRequest(String documentKey) {
        return GetObjectRequest.builder()
                .bucket(bucket)
                .key(documentKey)
                .build();
    }

    public GetObjectPresignRequest buildPresignReqeust(int expirationMinutes, GetObjectRequest getObjectRequest) {
        return GetObjectPresignRequest.builder()
                .getObjectRequest(getObjectRequest)
                .signatureDuration(Duration.ofMinutes(expirationMinutes))
                .build();
    }
}
