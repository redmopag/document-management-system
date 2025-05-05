package com.redmopag.documentmanagment.documentservice.service.storage;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String upload(MultipartFile file);

    String generateLing(String objectKey);
}
