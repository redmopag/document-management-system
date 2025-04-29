package com.redmopag.documentmanagment.ocrservice.service;

import com.redmopag.documentmanagment.ocrservice.dto.OcrResponse;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface OcrService {
    OcrResponse recognize(MultipartFile file) throws IOException, TesseractException;
}
