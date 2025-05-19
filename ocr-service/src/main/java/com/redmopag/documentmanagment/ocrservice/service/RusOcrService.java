package com.redmopag.documentmanagment.ocrservice.service;

import com.redmopag.documentmanagment.common.*;
import com.redmopag.documentmanagment.ocrservice.exception.OcrFailedException;
import lombok.RequiredArgsConstructor;
import net.sourceforge.tess4j.*;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
public class RusOcrService implements OcrService{
    private final ITesseract tesseract;
    private final PdfService pdfService;

    @Override
    public ProcessTextEvent recognize(OcrFileEvent event) {
        var downloadFile = pdfService.downloadFile(event.getDownloadUrl(), event.getFilePostfix());
        String hocrContent = recognizeText(downloadFile);
        String plainText = Jsoup.parse(hocrContent).text();
        System.out.println("Документ " + event.getFileId() + " распознан");
        downloadFile.delete();
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
            throw new OcrFailedException("Не удалось распознать текст: " + e.getMessage());
        }
    }
}
