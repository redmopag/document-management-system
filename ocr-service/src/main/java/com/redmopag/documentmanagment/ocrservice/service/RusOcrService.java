package com.redmopag.documentmanagment.ocrservice.service;

import com.redmopag.documentmanagment.ocrservice.client.FileDownloader;
import com.redmopag.documentmanagment.ocrservice.dto.*;
import com.redmopag.documentmanagment.ocrservice.exception.*;
import com.redmopag.documentmanagment.ocrservice.utils.DateExtractor;
import net.sourceforge.tess4j.*;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.util.List;

@Service
public class RusOcrService implements OcrService{
    private final ITesseract tesseract;
    private final FileDownloader fileDownloader;

    public RusOcrService(ITesseract tesseract, FileDownloader fileDownloader) {
        this.tesseract = tesseract;
        this.fileDownloader = fileDownloader;
    }

    @Override
    public ProcessTextEvent recognize(OcrFileEvent event) {
        File tempFile = fileDownloader.downloadFile(event.getDownloadUrl());
        String hocrContent = recognizeText(tempFile);
        String plainText = Jsoup.parse(hocrContent).text();
        List<LocalDate> expirationDates = DateExtractor.findAllDates(plainText);
        LocalDate expirationDate =
                expirationDates.isEmpty() ? null : expirationDates.get(expirationDates.size() - 1);
        String suggestedCategory = "MOCK";
        return ProcessTextEvent.builder()
                .fileId(event.getFileId())
                .objectKey(event.getObjectKey())
                .text(plainText)
                .hocrContent(hocrContent)
                .build();
    }

    private String recognizeText(File tempFile) {
        try {
            return tesseract.doOCR(tempFile);
        } catch (TesseractException e) {
            throw new OcrFailedException("Ошибка при распознавании текста: " + e.getMessage());
        }
    }
}
