package com.redmopag.documentmanagment.documentservice.controller;

import com.redmopag.documentmanagment.documentservice.dto.document.*;
import com.redmopag.documentmanagment.documentservice.dto.document.update.UpdateMetadataRequest;
import com.redmopag.documentmanagment.documentservice.dto.document.upload.UploadDocumentRequest;
import com.redmopag.documentmanagment.documentservice.service.document.DocumentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

@Validated
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
            @Valid @ModelAttribute UploadDocumentRequest uploadRequest) throws IOException {
        return documentService.uploadDocument(uploadRequest);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<DocumentSummaryResponse> getAllDocuments() {
        return documentService.getAllDocuments();
    }

    @GetMapping("/details")
    @ResponseStatus(HttpStatus.OK)
    public DocumentDetailsResponse getDocumentDetails(@RequestParam("id") @Min(1) Long id) {
        return documentService.getDocumentDetails(id);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<DocumentSummaryResponse> searchDocument(
            @RequestParam("username") String userName,
            @RequestParam("text") String text) {
        return documentService.findDocumentByText(userName, text);
    }

    @DeleteMapping("delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDocument(@RequestParam("id") @Min(1) Long id) {
        documentService.deleteDocument(id);
    }

    @PatchMapping("/update")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateDocumentMetadata(@RequestBody @Valid UpdateMetadataRequest request) {
        documentService.updateDocument(request);
    }

    @GetMapping("/status/stream")
    public SseEmitter streamStatusUpdate() {
        return documentService.registerEmitter();
    }
}
