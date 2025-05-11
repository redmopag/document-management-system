package com.redmopag.documentmanagment.filestorageservice.client.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.*;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DocumentStorageClient {
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final S3Director s3Director;

    @Value("${file.storage.bucket}")
    private String bucket;

    public String upload(byte[] content, String fileName) {
        String documentKey = UUID.randomUUID() + "-" + fileName;
        PutObjectRequest putObjectRequest = s3Director.buildPutRequest(bucket, documentKey);
        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(content));
        return documentKey;
    }

    public String generatePresignedUrl(String documentKey, int expirationMinutes) {
        GetObjectRequest getObjectRequest = s3Director.buildGetRequest(bucket, documentKey);
        GetObjectPresignRequest presignRequest = s3Director.buildPresignReqeust(expirationMinutes, getObjectRequest);
        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
        System.out.println("Presigned URL: " + presignedRequest.url().toString());
        return presignedRequest.url().toExternalForm();
    }

    public void deleteFile(String objectKey) {
        DeleteObjectRequest deleteObjectRequest = s3Director.buildDeleteRequest(bucket, objectKey);
        var response = s3Client.deleteObject(deleteObjectRequest);
        System.out.println("Удаление файла: " + objectKey + "\nФлаг удаления: " + response.deleteMarker());
    }
}
