package com.redmopag.documentmanagment.documentservice.service.storage;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StorageService {
    void upload(Long fileId, List<MultipartFile> files);

    String getDownloadUrl(String objectKey);
}
