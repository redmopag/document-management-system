package com.redmopag.documentmanagment.documentservice.service.storage;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    void upload(Long fileId, MultipartFile file);

    String getDownloadUrl(String objectKey);
}
