package com.redmopag.documentmanagment.documentservice.controller;

import com.redmopag.documentmanagment.documentservice.dto.UploadDocumentResponse;
import com.redmopag.documentmanagment.documentservice.service.DocumentService;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/documents")
public class DocumentController {
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public UploadDocumentResponse uploadDocument(@RequestParam("file") MultipartFile file)
            throws IOException {
        return documentService.uploadDocument(file);
    }
}
