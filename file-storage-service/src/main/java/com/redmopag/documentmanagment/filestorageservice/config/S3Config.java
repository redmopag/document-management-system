package com.redmopag.documentmanagment.filestorageservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

@Configuration
public class S3Config {
    @Value("${file.storage.url}")
    private String minIoUrl;

    @Bean
    public StaticCredentialsProvider credentialsProvider() {
        return StaticCredentialsProvider.create(
                AwsBasicCredentials.create("minioadmin", "minioadmin"));
    }

    @Bean
    public S3Client s3Client(StaticCredentialsProvider credentialsProvider) {
        return S3Client.builder()
                .endpointOverride(URI.create(minIoUrl))
                .region(Region.EU_WEST_3)
                .credentialsProvider(credentialsProvider)
                .forcePathStyle(true)
                .build();
    }

    @Bean
    public S3Presigner s3Presigner(StaticCredentialsProvider credentialsProvider) {
        return S3Presigner.builder()
                .endpointOverride(URI.create(minIoUrl))
                .region(Region.EU_WEST_3)
                .credentialsProvider(credentialsProvider)
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build())
                .build();
    }
}
