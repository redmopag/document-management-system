package com.redmopag.documentmanagment.filestorageservice.controller;

import com.redmopag.documentmanagment.common.GenerateLinkResponse;
import com.redmopag.documentmanagment.filestorageservice.service.storage.StorageService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/storage/file")
public class StorageController {
    private final StorageService storageService;

    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void upload(@RequestParam("file-id") Long fileId,
                       @RequestParam("file") MultipartFile file) {
        storageService.upload(fileId, file);
    }

    @GetMapping("/link")
    @ResponseStatus(HttpStatus.CREATED)
    public GenerateLinkResponse generateLink(@RequestParam("object-key") String objectKey) {
        return storageService.generateLink(objectKey);
    }
}
