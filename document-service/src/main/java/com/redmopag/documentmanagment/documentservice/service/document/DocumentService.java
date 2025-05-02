package com.redmopag.documentmanagment.documentservice.service.document;

import com.redmopag.documentmanagment.documentservice.dto.UploadDocumentResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface DocumentService {
    UploadDocumentResponse uploadDocument(MultipartFile file) throws IOException;
}
