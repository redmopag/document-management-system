package com.redmopag.documentmanagment.documentservice.service.document;

import com.redmopag.documentmanagment.documentservice.client.OcrClient;
import com.redmopag.documentmanagment.documentservice.dto.OcrResponse;
import com.redmopag.documentmanagment.documentservice.dto.document.*;
import com.redmopag.documentmanagment.documentservice.exception.*;
import com.redmopag.documentmanagment.documentservice.model.*;
import com.redmopag.documentmanagment.documentservice.repository.DocumentRepository;
import com.redmopag.documentmanagment.documentservice.service.FileType;
import com.redmopag.documentmanagment.documentservice.service.storage.StorageService;
import com.redmopag.documentmanagment.documentservice.service.text.DocumentTextService;
import com.redmopag.documentmanagment.documentservice.utils.DocumentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    private final OcrClient ocrClient;
    private final StorageService storageService;
    private final DocumentTextService documentTextService;

    //TODO: вынести оркестрацию в отдельный класс
    @Override
    public DocumentSummaryResponse uploadDocument(MultipartFile file) throws IOException {
        validateMimeType(file);
        String documentKey = storageService.upload(file);
        OcrResponse recognitionResult = ocrClient.recognizeFile(file);
        Document documentToSave = buildDocument(file, documentKey, recognitionResult);
        Document savedDocument = documentRepository.save(documentToSave);
        documentTextService.saveText(savedDocument.getId(), recognitionResult.getText(),
                recognitionResult.getHocrContent());
        return DocumentMapper.INSTANCE.toDocumentSummaryResponse(savedDocument);
    }

    private void validateMimeType(MultipartFile file) {
        if(!FileType.isValidMimeType(file.getContentType())) {
            throw new DocumentParamsException("Invalid mime type of document with name: " +
                    file.getOriginalFilename());
        }
    }

    private Document buildDocument(MultipartFile file, String storagePath,
                                          OcrResponse recognitionResult) {
        return Document.builder()
                .name(file.getOriginalFilename())
                .objectKey(storagePath)
                .category(recognitionResult.getCategory())
                .expirationDate(recognitionResult.getExpirationDate())
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
        DocumentText docText = documentTextService.getTextById(id);
        String originalDocUrl = storageService.generateLing(doc.getObjectKey());
        return buildDocumentDetailsResponse(doc, originalDocUrl, docText);
    }

    private Document getDocumentById(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException("Документ не найден"));
    }

    private static DocumentDetailsResponse buildDocumentDetailsResponse(
            Document doc, String originalDocUrl, DocumentText docText) {
        return DocumentDetailsResponse.builder()
                .id(doc.getId())
                .name(doc.getName())
                .category(doc.getCategory())
                .createdAt(doc.getUploadedAt())
                .lastModified(doc.getUpdatedAt())
                .downloadUrl(originalDocUrl)
                .hocrText(docText.getHocrContent())
                .build();
    }
}
