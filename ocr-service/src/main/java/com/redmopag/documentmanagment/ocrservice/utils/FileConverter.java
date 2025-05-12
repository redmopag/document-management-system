package com.redmopag.documentmanagment.ocrservice.utils;

import com.redmopag.documentmanagment.ocrservice.exception.OcrFailedException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.*;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

public class FileConverter {
    public static File convertToFile(Mat preprocessImage) {
        try {
            File tempFile = File.createTempFile("preprocessed", ".png");
            Imgcodecs.imwrite(tempFile.getAbsolutePath(), preprocessImage);
            return tempFile;
        } catch (IOException e) {
            throw new OcrFailedException("Не удалось предобработать файл: " + e.getMessage());
        }
    }

    public static List<File> convertToImages(File pdfFile) {
        try (PDDocument document = Loader.loadPDF(pdfFile)) {
            List<File> imageFiles = new ArrayList<>();
            renderDocument(document, imageFiles);
            return imageFiles;
        } catch (IOException e) {
            throw new OcrFailedException(e.getMessage());
        }
    }

    private static void renderDocument(PDDocument document, List<File> imageFiles) throws IOException {
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        int pageCount = document.getNumberOfPages();
        for (int page = 0; page < pageCount; page++) {
            renderPageToImage(pdfRenderer, page, imageFiles);
        }
    }

    private static void renderPageToImage(PDFRenderer pdfRenderer, int page, List<File> imageFiles) throws IOException {
        BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
        File imageFile = File.createTempFile("page_" + page + "_", ".png");
        ImageIO.write(bim, "png", imageFile);
        imageFiles.add(imageFile);
    }
}
