package com.redmopag.documentmanagment.documentservice.service.document;

import com.redmopag.documentmanagment.documentservice.dto.UploadDocumentResponse;
import com.redmopag.documentmanagment.documentservice.model.Document;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface DocumentService {
    UploadDocumentResponse uploadDocument(MultipartFile file) throws IOException;

    List<Document> findExpiringAt(LocalDate expirationDate);
}
