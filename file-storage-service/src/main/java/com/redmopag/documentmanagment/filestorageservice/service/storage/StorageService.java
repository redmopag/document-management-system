package com.redmopag.documentmanagment.filestorageservice.service.storage;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    void upload(Long fileId, MultipartFile file);

    String generateLink(String objectKey);
}
