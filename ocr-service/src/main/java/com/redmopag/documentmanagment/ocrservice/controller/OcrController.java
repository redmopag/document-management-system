package com.redmopag.documentmanagment.ocrservice.controller;

import com.redmopag.documentmanagment.ocrservice.dto.OcrResponse;
import com.redmopag.documentmanagment.ocrservice.service.RusOcrService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/ocr")
public class OcrController {
    private final RusOcrService ocrService;

    public OcrController(RusOcrService ocrService) {
        this.ocrService = ocrService;
    }
}
