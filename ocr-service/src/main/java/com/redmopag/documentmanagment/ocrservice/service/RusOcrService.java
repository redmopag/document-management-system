package com.redmopag.documentmanagment.ocrservice.service;

import com.redmopag.documentmanagment.ocrservice.dto.OcrResponse;
import com.redmopag.documentmanagment.ocrservice.utils.DateExtractor;
import net.sourceforge.tess4j.*;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDate;
import java.util.List;

@Service
public class RusOcrService {
    private final ITesseract tesseract;

    private String hocrContent;
    private String plainText;
    private List<LocalDate> expirationDates;
    private LocalDate expirationDate;
    private String suggestedCategory;

    public RusOcrService(ITesseract tesseract) {
        this.tesseract = tesseract;
    }

    public OcrResponse recognize(MultipartFile file) throws IOException, TesseractException {
        File tempFile = prepareFile(file);
        recognizeText(tempFile);
        extractExpirationDates();
        suggestedCategory = "MOCK";
        return buildOcrResponse();
    }

    private static File prepareFile(MultipartFile file) throws IOException {
        File tempFile = File.createTempFile("upload", file.getOriginalFilename());
        file.transferTo(tempFile);
        return tempFile;
    }

    private void recognizeText(File tempFile) throws TesseractException {
        hocrContent = tesseract.doOCR(tempFile);
        plainText = Jsoup.parse(hocrContent).text();
    }

    private void extractExpirationDates() {
        expirationDates = DateExtractor.findAllDates(plainText);
        expirationDate = expirationDates.isEmpty() ? null : expirationDates.get(expirationDates.size() - 1);
    }

    private OcrResponse buildOcrResponse() {
        return OcrResponse.builder()
                .hocrContent(hocrContent)
                .text(plainText)
                .expirationDate(expirationDate)
                .expirationDates(expirationDates)
                .category(suggestedCategory)
                .build();
    }
}
