package com.redmopag.documentmanagment.ocrservice.controller;

import com.redmopag.documentmanagment.ocrservice.dto.OcrResponse;
import com.redmopag.documentmanagment.ocrservice.service.RusOcrService;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/ocr")
public class OcrController {
    private final RusOcrService ocrService;

    public OcrController(RusOcrService ocrService) {
        this.ocrService = ocrService;
    }

    @PostMapping("/recognize")
    @ResponseStatus(HttpStatus.OK)
    public OcrResponse recognizeText(@RequestParam("file") MultipartFile file)
            throws TesseractException, IOException {
        return ocrService.recognize(file);
    }
}
