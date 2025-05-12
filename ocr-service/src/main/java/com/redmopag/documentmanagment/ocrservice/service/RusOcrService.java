package com.redmopag.documentmanagment.ocrservice.service;

import com.redmopag.documentmanagment.common.*;
import com.redmopag.documentmanagment.ocrservice.exception.OcrFailedException;
import net.sourceforge.tess4j.*;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class RusOcrService implements OcrService{
    private final ITesseract tesseract;
    private final PreprocessServiceImpl preprocessServiceImpl;

    public RusOcrService(ITesseract tesseract,
                         PreprocessServiceImpl preprocessServiceImpl) {
        this.tesseract = tesseract;
        this.preprocessServiceImpl = preprocessServiceImpl;
    }

    @Override
    public ProcessTextEvent recognize(OcrFileEvent event) {
        var tempFile = preprocessServiceImpl.preprocess(event.getDownloadUrl(), event.getFilePostfix());
        String hocrContent = recognizeText(tempFile);
        String plainText = Jsoup.parse(hocrContent).text();
        System.out.println("Документ " + event.getFileId() + " распознан");
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
