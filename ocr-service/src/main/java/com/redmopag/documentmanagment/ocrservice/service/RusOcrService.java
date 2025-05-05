package com.redmopag.documentmanagment.ocrservice.service;

import com.redmopag.documentmanagment.ocrservice.dto.OcrResponse;
import com.redmopag.documentmanagment.ocrservice.exception.*;
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

    public RusOcrService(ITesseract tesseract) {
        this.tesseract = tesseract;
    }

    public OcrResponse recognize(MultipartFile file) {
        File tempFile = prepareFile(file);
        String hocrContent = recognizeText(tempFile);
        String plainText = Jsoup.parse(hocrContent).text();
        List<LocalDate> expirationDates = DateExtractor.findAllDates(plainText);
        LocalDate expirationDate =
                expirationDates.isEmpty() ? null : expirationDates.get(expirationDates.size() - 1);
        String suggestedCategory = "MOCK";
        return OcrResponse.builder()
                .hocrContent(hocrContent)
                .text(plainText)
                .expirationDate(expirationDate)
                .expirationDates(expirationDates)
                .category(suggestedCategory)
                .build();
    }

    private File prepareFile(MultipartFile file) {
        try {
            File tempFile = File.createTempFile("upload", file.getOriginalFilename());
            file.transferTo(tempFile);
            return tempFile;
        } catch (IOException e) {
            throw new InvalidFileException("Не удалось прочитать файл: " +
                    file.getOriginalFilename() + " " + e.getMessage());
        }
    }

    private String recognizeText(File tempFile) {
        try {
            return tesseract.doOCR(tempFile);
        } catch (TesseractException e) {
            throw new OcrFailedException("Ошибка при распознавании текста: " + e.getMessage());
        }
    }
}
