package com.redmopag.documentmanagment.documentservice.service.storage;

import com.redmopag.documentmanagment.common.GenerateLinkResponse;
import com.redmopag.documentmanagment.documentservice.client.FileStorageClient;
import com.redmopag.documentmanagment.documentservice.exception.badrequest.InvalidFileException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class StorageServiceImpl implements StorageService {
    private final FileStorageClient fileStorageClient;

    public StorageServiceImpl(FileStorageClient fileStorageClient) {
        this.fileStorageClient = fileStorageClient;
    }

    @Override
    public void upload(Long fileId, MultipartFile file) {
        try {
            fileStorageClient.sendFile(fileId, file).subscribe();
        } catch (IOException e) {
            throw new InvalidFileException(e.getMessage());
        }
    }

    @Override
    public String getDownloadUrl(String objectKey) {
        GenerateLinkResponse response = fileStorageClient.generatePresignedUrl(objectKey);
        return response.getDownloadUrl();
    }
}
