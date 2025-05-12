package com.redmopag.documentmanagment.ocrservice.service;

import com.redmopag.documentmanagment.common.*;
import com.redmopag.documentmanagment.ocrservice.client.FileDownloader;
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
    private final PreprocessServiceImpl preprocessServiceImpl;
    private final FileDownloader fileDownloader;

    @Override
    public ProcessTextEvent recognize(OcrFileEvent event) {
        var downloadFile = downloadFile(event.getDownloadUrl(), event.getFilePostfix());
        var preprocessedFile = preprocessServiceImpl.preprocess(downloadFile);
        String hocrContent = recognizeText(preprocessedFile);
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

    private File downloadFile(String downloadUrl, String filePostfix) {
        System.out.println("Скачивание файла по url: " + downloadUrl);
        File tempFile = fileDownloader.downloadFile(downloadUrl, filePostfix);
        System.out.println("Скачан файл: " + tempFile.getName());
        return tempFile;
    }
}
