package com.redmopag.documentmanagment.documentservice.controller;

import com.redmopag.documentmanagment.documentservice.dto.document.*;
import com.redmopag.documentmanagment.documentservice.service.document.DocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/documents")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class DocumentController {
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public DocumentSummaryResponse uploadDocument(@RequestParam("file") MultipartFile file)
            throws IOException {
        return documentService.uploadDocument(file);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<DocumentSummaryResponse> getAllDocuments() {
        return documentService.getAllDocuments();
    }

    @GetMapping("/details")
    @ResponseStatus(HttpStatus.OK)
    public DocumentDetailsResponse getDocumentDetails(@RequestParam("id") Long id) {
        return documentService.getDocumentDetails(id);
    }
}
