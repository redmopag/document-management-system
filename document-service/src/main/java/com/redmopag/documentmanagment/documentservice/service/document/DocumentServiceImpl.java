package com.redmopag.documentmanagment.documentservice.service.document;

import com.redmopag.documentmanagment.documentservice.client.*;
import com.redmopag.documentmanagment.documentservice.dto.*;
import com.redmopag.documentmanagment.documentservice.exception.DocumentParamsException;
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

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    private final OcrClient ocrClient;
    private final StorageService storageService;
    private final DocumentTextService documentTextService;

    public UploadDocumentResponse uploadDocument(MultipartFile file) throws IOException {
        validateMimeType(file);
        String objectKey = storageService.upload(file);
        OcrResponse recognitionResult = ocrClient.recognizeText(file.getBytes(), file.getOriginalFilename());
        Document documentToSave = buildDocument(file, objectKey, recognitionResult);
        Document savedDocument = documentRepository.save(documentToSave);
        documentTextService.saveText(savedDocument.getId(), recognitionResult.getText(),
                recognitionResult.getHocrContent());
        return DocumentMapper.INSTANCE.toUploadDocumentResponse(savedDocument);
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
}
