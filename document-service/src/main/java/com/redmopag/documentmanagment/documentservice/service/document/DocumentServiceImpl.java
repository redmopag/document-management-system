package com.redmopag.documentmanagment.documentservice.service.document;

import com.redmopag.documentmanagment.common.*;
import com.redmopag.documentmanagment.documentservice.dto.document.*;
import com.redmopag.documentmanagment.documentservice.exception.*;
import com.redmopag.documentmanagment.documentservice.model.*;
import com.redmopag.documentmanagment.documentservice.repository.DocumentRepository;
import com.redmopag.documentmanagment.documentservice.service.FileType;
import com.redmopag.documentmanagment.documentservice.service.storage.StorageService;
import com.redmopag.documentmanagment.documentservice.service.text.TextService;
import com.redmopag.documentmanagment.documentservice.utils.DocumentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    private final StorageService storageService;
    private final TextService textService;

    @Override
    public DocumentSummaryResponse uploadDocument(MultipartFile file) {
        validateMimeType(file);
        var savedDocument = documentRepository.save(buildDocument(file));
        storageService.upload(savedDocument.getId(), file);
        System.out.println("Сохранён документ: " + savedDocument.getName() + " - " + savedDocument.getId());
        return DocumentMapper.INSTANCE.toDocumentSummaryResponse(savedDocument);
    }

    private void validateMimeType(MultipartFile file) {
        if(!FileType.isValidMimeType(file.getContentType())) {
            throw new DocumentParamsException("Invalid mime type of document with name: " +
                    file.getOriginalFilename());
        }
    }

    private Document buildDocument(MultipartFile file) {
        return Document.builder()
                .name(file.getOriginalFilename())
                .status(DocumentStatus.PROCESSING)
                .build();
    }

    @Override
    public List<Document> findExpiringAt(LocalDate expirationDate) {
        return documentRepository.findDocumentByExpirationDate(expirationDate);
    }

    @Override
    public List<DocumentSummaryResponse> getAllDocuments() {
        return documentRepository.findAll().stream()
                .map(DocumentMapper.INSTANCE::toDocumentSummaryResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DocumentDetailsResponse getDocumentDetails(Long id) {
        Document doc = getDocumentById(id);
        TextResponse docText = textService.getTextById(doc.getTextId());
        String originalDocUrl = storageService.getDownloadUrl(doc.getObjectKey());
        return buildDocumentDetailsResponse(doc, originalDocUrl, docText.getHocrContent());
    }

    private DocumentDetailsResponse buildDocumentDetailsResponse(
            Document doc, String originalDocUrl, String hocrContent) {
        return DocumentDetailsResponse.builder()
                .id(doc.getId())
                .name(doc.getName())
                .createdAt(doc.getUploadedAt())
                .lastModified(doc.getUpdatedAt())
                .downloadUrl(originalDocUrl)
                .hocrText(hocrContent)
                .build();
    }

    @Override
    public List<DocumentSummaryResponse> getDocumentsByContaining(String text) {
        List<TextResponse> docText = textService.getTextByContaining(text);
        if (docText.isEmpty()) {
            return Collections.emptyList();
        }
        Iterable<Long> ids = docText.stream().map(TextResponse::getDocumentId).collect(Collectors.toList());
        return documentRepository.findAllById(ids)
                .stream()
                .map(DocumentMapper.INSTANCE::toDocumentSummaryResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void updateDocumentMetadata(MetadataEvent event) {
        var document = updateDocument(event);
        documentRepository.save(document);
        System.out.println("Метаданные документа " + document.getName() +
                " - " + document.getId() + " обновлены");
    }

    private Document updateDocument(MetadataEvent event) {
        Document document = getDocumentById(event.getDocumentId());
        document.setUpdatedAt(LocalDateTime.now());
        document.setObjectKey(event.getObjectKey());
        document.setTextId(event.getTextId());
        document.setStatus(DocumentStatus.CONFIRMED);
        return document;
    }

    private Document getDocumentById(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException(id));
    }
}
