package com.redmopag.documentmanagment.filestorageservice.service.storage;

import com.redmopag.documentmanagment.common.*;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    void upload(Long fileId, MultipartFile file);

    GenerateLinkResponse generateLink(String objectKey);

    void deleteFile(DeletedFile file);
}
