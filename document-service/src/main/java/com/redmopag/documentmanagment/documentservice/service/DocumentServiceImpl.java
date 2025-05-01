package com.redmopag.documentmanagment.documentservice.service;

import com.redmopag.documentmanagment.documentservice.client.*;
import com.redmopag.documentmanagment.documentservice.dto.*;
import com.redmopag.documentmanagment.documentservice.exception.DocumentParamsException;
import com.redmopag.documentmanagment.documentservice.model.*;
import com.redmopag.documentmanagment.documentservice.repository.DocumentRepository;
import com.redmopag.documentmanagment.documentservice.utils.DocumentMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    private final OcrClient ocrClient;
    private final DocumentStorageClient documentStorageClient;

    public DocumentServiceImpl(DocumentRepository documentRepository, OcrClient ocrClient, DocumentStorageClient documentStorageClient) {
        this.documentRepository = documentRepository;
        this.ocrClient = ocrClient;
        this.documentStorageClient = documentStorageClient;
    }

    public UploadDocumentResponse uploadDocument(MultipartFile file) throws IOException {
        validateMimeType(file);
        String storagePath = documentStorageClient.upload(file.getBytes(), file.getOriginalFilename());
        OcrResponse recognitionResult = ocrClient.recognizeText(file.getBytes(), file.getOriginalFilename());
        Document documentToSave = buildDocument(file, storagePath, recognitionResult);
        Document savedDocument = documentRepository.save(documentToSave);
        return DocumentMapper.INSTANCE.toUploadDocumentResponse(savedDocument);
    }

    private static void validateMimeType(MultipartFile file) {
        if(!FileType.isValidMimeType(file.getContentType())) {
            throw new DocumentParamsException("Invalid mime type of document with name: " +
                    file.getOriginalFilename());
        }
    }

    private static Document buildDocument(MultipartFile file, String storagePath, OcrResponse recognitionResult) {
        return Document.builder()
                .name(file.getOriginalFilename())
                .storagePath(storagePath)
                .textId(recognitionResult.getTextId())
                .type(recognitionResult.getSuggestedType())
                .expirationDate(recognitionResult.getSuggestedExpirationDate())
                .status(DocumentStatus.PROCESSING)
                .build();
    }
}
