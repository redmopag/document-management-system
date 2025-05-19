package com.redmopag.documentmanagment.ocrservice.service;

import com.redmopag.documentmanagment.ocrservice.client.FileDownloader;
import com.redmopag.documentmanagment.ocrservice.exception.FileProcessedException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class PdfService {
    private final FileDownloader fileDownloader;

    public PdfService(FileDownloader fileDownloader) {
        this.fileDownloader = fileDownloader;
    }

    public File downloadFile(String downloadUrl, String filePostfix) {
        System.out.println("Скачивание файла по url: " + downloadUrl);
        File tempFile = fileDownloader.downloadFile(downloadUrl, filePostfix);
        System.out.println("Скачан файл: " + tempFile.getName());
        return tempFile;
    }

    public boolean doesContainImages(File pdfFile) {
        try (PDDocument file = Loader.loadPDF(pdfFile)) {
            for (var page : file.getPages()) {
                PDResources resources = page.getResources();
                for (var name : resources.getXObjectNames()) {
                    var xObject = resources.getXObject(name);
                    if (xObject instanceof PDImageXObject) {
                        return true;
                    }
                }
            }
            return false;
        } catch (IOException e) {
            throw new FileProcessedException("Не удалось обработать файл: " + e);
        }
    }

    public String extractText(File pdfFile) {
        try(PDDocument file = Loader.loadPDF(pdfFile)) {
            return new PDFTextStripper().getText(file);
        } catch (IOException e) {
            throw new FileProcessedException("Не удалось извлечь текст из файла: " + e);
        }
    }
}
