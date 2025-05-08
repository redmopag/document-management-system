package com.redmopag.documentmanagment.ocrservice.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.*;

@Component
public class FileDownloader {
    private final WebClient webClient = WebClient.builder().build();

    public File downloadFile(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(InputStream.class)
                .flatMap(inputStream -> {
                    try {
                        File tempFile = File.createTempFile("download-", ".tmp");
                        try (FileOutputStream outputStream = new FileOutputStream(tempFile)){
                            inputStream.transferTo(outputStream);
                        }
                        return Mono.just(tempFile);
                    } catch (IOException e) {
                        return Mono.error(e);
                    }
                })
                .block();
    }
}
