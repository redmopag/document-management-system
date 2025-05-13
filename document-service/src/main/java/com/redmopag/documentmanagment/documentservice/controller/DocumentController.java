package com.redmopag.documentmanagment.documentservice.controller;

import com.redmopag.documentmanagment.documentservice.dto.document.*;
import com.redmopag.documentmanagment.documentservice.service.document.DocumentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDate;
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
    public DocumentSummaryResponse uploadDocument(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "expirationDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expirationDate,
            @RequestParam("username") String userName)
            throws IOException {
        return documentService.uploadDocument(files, category, expirationDate, userName);
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

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<DocumentSummaryResponse> searchDocument(
            @RequestParam("username") String userName,
            @RequestParam("text") String text) {
        return documentService.getDocumentsByContaining(userName, text);
    }

    @DeleteMapping("delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDocument(@RequestParam("id") Long id) {
        documentService.deleteDocument(id);
    }

    @PatchMapping("/update")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateDocumentMetadata(@RequestBody UpdateMetadataRequest request) {
        documentService.updateDocument(request);
    }

    @GetMapping("/status/stream")
    public SseEmitter streamStatusUpdate() {
        return documentService.registerEmitter();
    }
}
