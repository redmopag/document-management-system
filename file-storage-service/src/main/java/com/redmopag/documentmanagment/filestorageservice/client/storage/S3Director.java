package com.redmopag.documentmanagment.filestorageservice.client.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.time.Duration;

@Component
public class S3Director {
    public PutObjectRequest buildPutRequest(String bucketName, String documentKey) {
        return PutObjectRequest.builder()
                .bucket(bucketName)
                .key(documentKey)
                .contentType("application/octet-stream")
                .build();
    }

    public GetObjectRequest buildGetRequest(String bucketName, String documentKey) {
        return GetObjectRequest.builder()
                .bucket(bucketName)
                .key(documentKey)
                .build();
    }

    public GetObjectPresignRequest buildPresignReqeust(int expirationMinutes, GetObjectRequest getObjectRequest) {
        return GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(expirationMinutes))
                .getObjectRequest(getObjectRequest)
                .build();
    }
}
