package com.redmopag.documentmanagment.filestorageservice.service.storage;

import com.redmopag.documentmanagment.common.*;
import com.redmopag.documentmanagment.filestorageservice.kafka.producer.OcrKafkaProducer;
import com.redmopag.documentmanagment.filestorageservice.client.storage.DocumentStorageClient;
import com.redmopag.documentmanagment.filestorageservice.exception.InvalidFileException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class StorageServiceImpl implements StorageService {
    private final static int EXPIRATION_MINUTES = 10;

    private final DocumentStorageClient documentStorageClient;
    private final OcrKafkaProducer ocrKafkaProducer;

    public StorageServiceImpl(DocumentStorageClient documentStorageClient, OcrKafkaProducer ocrKafkaProducer) {
        this.documentStorageClient = documentStorageClient;
        this.ocrKafkaProducer = ocrKafkaProducer;
    }

    @Override
    public void upload(Long fileId, MultipartFile file) {
        try {
            String objectKey = uploadFile(file);
            var filePostfix = file.getOriginalFilename().split("\\.")[1];
            ocrKafkaProducer.ocrFile(fileId,
                    objectKey,
                    generatePresignedUrl(objectKey, EXPIRATION_MINUTES),
                    filePostfix);
            System.out.println("Документ " + fileId + " помещён в хранилище. Ключ: " + objectKey);
        } catch (IOException e) {
            throw new InvalidFileException(e.getMessage());
        }
    }

    private String uploadFile(MultipartFile file) throws IOException {
        byte[] content = file.getBytes();
        String fileName = file.getOriginalFilename();
        return documentStorageClient.upload(content, fileName);
    }

    @Override
    public GenerateLinkResponse generateLink(String objectKey) {
        var url = generatePresignedUrl(objectKey, EXPIRATION_MINUTES);
        return new GenerateLinkResponse(url);
    }

    public String generatePresignedUrl(String objectKey, int expirationMinutes) {
        var fileName = extractFileName(objectKey);
        return documentStorageClient.generatePresignedUrl(objectKey, expirationMinutes, fileName);
    }

    private String extractFileName(String objectKey) {
        int firstDash = objectKey.indexOf("-");
        if (firstDash >= 0 && firstDash < objectKey.length() - 1) {
            return objectKey.substring(firstDash + 1);
        }
        return objectKey;
    }

    @Override
    public void deleteFile(DeletedFile file) {
        documentStorageClient.deleteFile(file.getObjectKey());
    }
}
