package com.redmopag.documentmanagment.documentservice.service.document;

import com.redmopag.documentmanagment.common.MetadataEvent;
import com.redmopag.documentmanagment.documentservice.dto.document.*;
import com.redmopag.documentmanagment.documentservice.model.Document;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface DocumentService {
    DocumentSummaryResponse uploadDocument(List <MultipartFile> files, String category, LocalDate expirationDate)
            throws IOException;

    List<Document> findExpiringAt(LocalDate expirationDate);

    List<DocumentSummaryResponse> getAllDocuments();

    DocumentDetailsResponse getDocumentDetails(Long id);

    List<DocumentSummaryResponse> getDocumentsByContaining(String text);

    void updateDocumentMetadata(MetadataEvent event);

    void deleteDocument(Long id);

    void updateDocument(UpdateMetadataRequest request);

    SseEmitter registerEmitter();
}
