package com.redmopag.documentmanagment.documentservice.service.storage;

import com.redmopag.documentmanagment.documentservice.client.DocumentStorageClient;
import com.redmopag.documentmanagment.documentservice.exception.InvalidFileException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class StorageServiceImpl implements StorageService {
    private final DocumentStorageClient documentStorageClient;

    public StorageServiceImpl(DocumentStorageClient documentStorageClient) {
        this.documentStorageClient = documentStorageClient;
    }

    @Override
    public String upload(MultipartFile file) {
        try {
            byte[] content = file.getBytes();
            String fileName = file.getOriginalFilename();
            return documentStorageClient.upload(content, fileName);
        } catch (IOException e) {
            throw new InvalidFileException(e.getMessage());
        }
    }

    @Override
    public MultipartFile download(String objectKey) {
        return null;
    }


}
